package org.atomnuke.fallout.context;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.atomnuke.Nuke;
import org.atomnuke.NukeEnvironment;
import org.atomnuke.atombus.config.model.Binding;
import org.atomnuke.atombus.config.model.MessageActor;
import org.atomnuke.atombus.config.model.MessageSource;
import org.atomnuke.atombus.config.model.Parameter;
import org.atomnuke.atombus.config.model.Parameters;
import org.atomnuke.fallout.config.server.ServerConfigurationHandler;
import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.plugin.operation.OperationFailureException;
import org.atomnuke.sink.AtomSink;
import org.atomnuke.source.AtomSource;
import org.atomnuke.task.atom.AtomTask;
import org.atomnuke.task.context.AtomTaskContext;
import org.atomnuke.task.context.TaskContextImpl;
import org.atomnuke.task.manager.AtomTasker;
import org.atomnuke.util.TimeValueUtil;
import org.atomnuke.util.config.ConfigurationException;
import org.atomnuke.lifecycle.Reclaimable;
import org.atomnuke.service.introspection.ServicesInterrogator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class FalloutContextImpl implements FalloutContext {

   private static final Logger LOG = LoggerFactory.getLogger(FalloutContextImpl.class);

   private final ServicesInterrogator servicesInterrogator;
   private final NukeEnvironment nukeEnvironment;
   private final Map<String, Binding> bindings;
   private final Map<String, AtomTask> tasks;
   private final ActorManager actorManager;
   private final AtomTasker atomTasker;

   public FalloutContextImpl(Nuke nukeReference, ServicesInterrogator servicesInterrogator) {
      this.servicesInterrogator = servicesInterrogator;
      this.nukeEnvironment = nukeReference.nukeEnvironment();
      this.atomTasker = nukeReference.atomTasker();

      actorManager = new ActorManager();
      tasks = new HashMap<String, AtomTask>();
      bindings = new HashMap<String, Binding>();
   }

   private static Map<String, String> parametersToMap(Parameters parameters) {
      final Map<String, String> paramMap = new HashMap<String, String>();

      if (parameters != null) {
         for (Parameter param : parameters.getParam()) {
            paramMap.put(param.getName(), param.getValue());
         }
      }

      return paramMap;
   }

   private boolean isActorBoundAsSource(String id) {
      for (Binding binding : bindings.values()) {
         if (binding.getSourceActor().equals(id)) {
            return true;
         }
      }

      return false;
   }

   private boolean isActorBoundToAnySource(String id) {
      for (Binding binding : bindings.values()) {
         if (binding.getSinkActor().equals(id)) {
            return true;
         }
      }

      return false;
   }

   @Override
   public void enlistActor(String name, InstanceContext<? extends Reclaimable> actor) {
      // We found an actor!
      LOG.debug("Found configuration for actor: " + name);

      // Do we already have a living reference to this actor?
      if (!actorManager.hasActor(name)) {
         // No? Let's add one then.
         actorManager.manageActor(name, (InstanceContext<Reclaimable>) actor);
      } else {
         // Debug log this case in case of problems
         LOG.debug("Actor already registered. Ignoring this instance.");
      }
   }

   @Override
   public void process(ServerConfigurationHandler cfgHandler) throws ConfigurationException {
      final List<Binding> bindingsToMerge = cfgHandler.getBindings();
      final Set<String> bindingsToBreak = new HashSet<String>(bindings.keySet());
      final Set<Binding> bindingsToAdd = new HashSet<Binding>();

      for (Binding binding : bindingsToMerge) {
         if (!bindingsToBreak.remove(binding.getId())) {
            bindingsToAdd.add(binding);
         }
      }

      for (Binding binding : bindingsToAdd) {
         bind(cfgHandler, binding);
      }

      for (String breakId : bindingsToBreak) {
         LOG.info("Breaking binding: " + breakId);
         bindings.remove(breakId);
      }

      garbageCollect();
   }

   private void garbageCollect() {
      final List<ActorEntry> garbageQueue = new LinkedList<ActorEntry>();

      for (String id : new HashSet<String>(actorManager.actorNames())) {
         // If this actor isn't bound to a source then it's doing nothing and should be removed
         if (!isActorBoundAsSource(id) && !isActorBoundToAnySource(id)) {
            retireActor(id, garbageQueue);
         }
      }

      for (ActorEntry actorEntry : garbageQueue) {
         actorEntry.cancel();
      }
   }

   private void retireActor(String id, List<ActorEntry> garbageQueue) {
      // Remove any lingering references - this might be a source
      tasks.remove(id);

      // Queue the actor for cancellation
      final ActorEntry actor = actorManager.removeActor(id);

      // If there's a cancellation remote, then this actor was initialized and needs to be garbage collected
      if (actor != null) {
         LOG.debug("Garbage collecting: " + actor);

         if (actor.initialized()) {
            garbageQueue.add(actor);
         }
      }
   }

   private void bind(ServerConfigurationHandler cfgHandler, Binding binding) throws ConfigurationException {
      LOG.info("Binding source " + binding.getSourceActor() + " to sink " + binding.getSinkActor());

      bind(getSourceTask(binding, cfgHandler), binding, cfgHandler);
   }

   private void bind(AtomTask source, Binding binding, ServerConfigurationHandler cfgHandler) throws ConfigurationException {
      final MessageActor sinkMessageActorDescriptor = cfgHandler.findMessageActor(binding.getSinkActor());
      final ActorEntry sinkActor = actorManager.getActor(binding.getSinkActor());

      if (sinkActor == null || sinkMessageActorDescriptor == null) {
         throw new ConfigurationException("Unable to locate actor, \"" + binding.getSinkActor() + "\" for usage as a sink.");
      }

      final Class instanceRefClass = sinkActor.instanceClass();

      if (!AtomSink.class.isAssignableFrom(instanceRefClass)) {
         throw new ConfigurationException("Actor, \"" + binding.getSinkActor() + "\" does not implement the AtomSink interface and can not be used as a sink.");
      }

      final AtomTaskContext taskContext = new TaskContextImpl(nukeEnvironment, LoggerFactory.getLogger(sinkMessageActorDescriptor.getId()),
              parametersToMap(sinkMessageActorDescriptor.getParameters()), servicesInterrogator, atomTasker, sinkMessageActorDescriptor.getId());

      try {
         sinkActor.init(taskContext);
      } catch (OperationFailureException ofe) {
         LOG.error("Unable to initialize sink: " + sinkMessageActorDescriptor.getId() + " with href: " + sinkMessageActorDescriptor.getHref() + " - Reason: " + ofe.getMessage(), ofe);
      }

      actorManager.setCancellationRemote(binding.getSinkActor(), source.addSink((InstanceContext<AtomSink>) sinkActor.instanceContext()));
      bindings.put(binding.getId(), binding);
   }

   private AtomTask getSourceTask(Binding binding, ServerConfigurationHandler cfgHandler) throws ConfigurationException {
      AtomTask sourceTask = tasks.get(binding.getSourceActor());

      return sourceTask != null ? sourceTask : registerTask(cfgHandler, binding);
   }

   private AtomTask registerTask(ServerConfigurationHandler cfgHandler, Binding binding) throws ConfigurationException {
      final MessageActor sourceMessageActorDescriptor = cfgHandler.findMessageActor(binding.getSourceActor());
      final MessageSource messageSourceDef = cfgHandler.findMessageSource(binding.getSourceActor());

      if (messageSourceDef == null || sourceMessageActorDescriptor == null) {
         throw new ConfigurationException("Actor, \"" + binding.getSinkActor() + "\" does not have a source definition within the configuration.");
      }

      final ActorEntry sourceActor = actorManager.getActor(sourceMessageActorDescriptor.getId());

      if (sourceActor == null) {
         throw new ConfigurationException("Unable to locate actor, \"" + sourceMessageActorDescriptor.getId() + "\" for usage as a source.");
      }

      // Reaching deep
      final Class instanceRefClass = sourceActor.instanceClass();

      if (!AtomSource.class.isAssignableFrom(instanceRefClass)) {
         throw new ConfigurationException("Actor, \"" + sourceMessageActorDescriptor.getId() + "\" does not implement the AtomSource interface and can not be used as a source.");
      }

      final AtomTaskContext taskContext = new TaskContextImpl(nukeEnvironment, LoggerFactory.getLogger(sourceMessageActorDescriptor.getId()),
              parametersToMap(sourceMessageActorDescriptor.getParameters()), servicesInterrogator, atomTasker, sourceMessageActorDescriptor.getId());

      try {
         sourceActor.init(taskContext);
      } catch (OperationFailureException ofe) {
         LOG.error("Unable to initialize source: " + sourceMessageActorDescriptor.getId() + " with href: " + sourceMessageActorDescriptor.getHref() + " - Reason: " + ofe.getMessage(), ofe);
      }

      final AtomTask sourceTask = atomTasker.follow((InstanceContext<AtomSource>) sourceActor.instanceContext(), TimeValueUtil.fromPollingInterval(messageSourceDef.getPollingInterval()));

      tasks.put(sourceMessageActorDescriptor.getId(), sourceTask);
      actorManager.setCancellationRemote(sourceMessageActorDescriptor.getId(), sourceTask.handle().cancellationRemote());

      return sourceTask;
   }
}
