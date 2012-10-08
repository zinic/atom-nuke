package org.atomnuke.container;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.atomnuke.Nuke;
import org.atomnuke.config.model.Binding;
import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.config.model.Eventlet;
import org.atomnuke.config.model.LanguageType;
import org.atomnuke.config.model.Parameter;
import org.atomnuke.config.model.Parameters;
import org.atomnuke.config.model.Relay;
import org.atomnuke.config.model.Sink;
import org.atomnuke.config.model.Source;
import org.atomnuke.container.config.ServerConfigurationHandler;
import org.atomnuke.container.context.ContainerContext;
import org.atomnuke.container.packaging.PackageContext;
import org.atomnuke.plugin.local.LocalInstanceEnvironment;
import org.atomnuke.listener.AtomListener;
import org.atomnuke.listener.eps.EventletRelay;
import org.atomnuke.listener.eps.eventlet.AtomEventlet;
import org.atomnuke.plugin.InstanceContextImpl;
import org.atomnuke.plugin.ReferenceInstantiationException;
import org.atomnuke.service.ServiceManager;
import org.atomnuke.source.AtomSource;
import org.atomnuke.task.Task;
import org.atomnuke.task.Tasker;
import org.atomnuke.task.context.TaskContextImpl;
import org.atomnuke.task.lifecycle.InitializationException;
import org.atomnuke.util.TimeValueUtil;
import org.atomnuke.util.config.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class ConfigurationProcessor {

   private static final Logger LOG = LoggerFactory.getLogger(ConfigurationProcessor.class);
   private final Collection<PackageContext> loadedPackages;
   private final ServerConfigurationHandler cfgHandler;
   private final ContainerContext containerContext;
   private final ServiceManager services;
   private final Tasker tasker;

   public ConfigurationProcessor(ServiceManager services, Tasker tasker, ContainerContext containerContext, ServerConfigurationHandler cfgHandler, Collection<PackageContext> loadedPackages) {
      this.services = services;
      this.tasker = tasker;
      this.containerContext = containerContext;
      this.cfgHandler = cfgHandler;
      this.loadedPackages = loadedPackages;
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

   public void merge(Nuke kernelBeingBuilt) throws ConfigurationException {
      LOG.info("Reading configuration");

      processSources(kernelBeingBuilt);
      processRelays();
      processListeners();
      processEventlets();

      containerContext.process(cfgHandler.getBindings());
   }

   public InstanceContext<AtomEventlet> constructEventlet(LanguageType langType, String ref) throws ReferenceInstantiationException {
      for (PackageContext packageContext : loadedPackages) {
         final InstanceContext<AtomEventlet> eventlet = packageContext.packageBindings().resolveEventlet(langType, ref);

         if (eventlet != null) {
            return eventlet;
         }
      }

      return null;
   }

   public InstanceContext<AtomSource> constructSource(LanguageType langType, String ref) throws ReferenceInstantiationException {
      for (PackageContext packageContext : loadedPackages) {
         final InstanceContext<AtomSource> source = packageContext.packageBindings().resolveSource(langType, ref);

         if (source != null) {
            return source;
         }
      }

      return null;
   }

   public InstanceContext<AtomListener> constructListener(LanguageType langType, String ref) throws ReferenceInstantiationException {
      for (PackageContext packageContext : loadedPackages) {
         final InstanceContext<AtomListener> listener = packageContext.packageBindings().resolveListener(langType, ref);

         if (listener != null) {
            return listener;
         }
      }

      return null;
   }

   public boolean hasSourceBinding(String name) throws ConfigurationException {
      for (Binding binding : cfgHandler.getBindings()) {
         if (binding.getTarget().equals(name)) {
            return true;
         }
      }

      return false;
   }

   public boolean hasListenerBinding(String name) throws ConfigurationException {
      for (Binding binding : cfgHandler.getBindings()) {
         if (binding.getReceiver().equals(name)) {
            return true;
         }
      }

      return false;
   }

   public void processSources(Nuke kernelBeingBuilt) throws ConfigurationException {
      for (Source source : cfgHandler.getSources()) {
         final String sourceId = source.getId();

         if (hasSourceBinding(sourceId) && !containerContext.hasTask(sourceId)) {
            try {
               final InstanceContext<AtomSource> sourceContext = constructSource(source.getType(), source.getHref());
               sourceContext.environment().stepInto();

               try {
                  sourceContext.instance().init(new TaskContextImpl(parametersToMap(source.getParameters()), services, tasker));
               } finally {
                  sourceContext.environment().stepOut();
               }

               final Task newTask = kernelBeingBuilt.follow(sourceContext, TimeValueUtil.fromPollingInterval(source.getPollingInterval()));
               containerContext.registerSource(source.getId(), newTask);
            } catch (ReferenceInstantiationException bie) {
               LOG.error("Could not create source instance " + source.getId() + ". Reason: " + bie.getMessage(), bie);
            } catch (InitializationException ie) {
               LOG.error("Could not initialize source instance " + source.getId() + ". Reason: " + ie.getMessage(), ie);
            }
         }
      }
   }

   public void processRelays() throws ConfigurationException {
      for (Relay relay : cfgHandler.getRelays()) {
         final String relayId = relay.getId();


         if (hasListenerBinding(relayId) && !containerContext.hasRelay(relayId)) {
            containerContext.registerRelay(relay.getId(), new InstanceContextImpl<EventletRelay>(LocalInstanceEnvironment.getInstance(), new EventletRelay()));
         }
      }
   }

   public void processListeners() throws ConfigurationException {
      for (Sink sink : cfgHandler.getSinks()) {
         final String sinkId = sink.getId();

         if (hasListenerBinding(sinkId) && !containerContext.hasSink(sinkId)) {
            try {
               final InstanceContext<AtomListener> listenerCtx = constructListener(sink.getType(), sink.getHref());
               listenerCtx.environment().stepInto();

               try {
                  listenerCtx.instance().init(new TaskContextImpl(parametersToMap(sink.getParameters()), services, tasker));
               } finally {
                  listenerCtx.environment().stepOut();
               }

               containerContext.registerSink(sink.getId(), listenerCtx);
            } catch (ReferenceInstantiationException bie) {
               LOG.error("Could not create sink instance " + sink.getId() + ". Reason: " + bie.getMessage(), bie);
               throw new ConfigurationException(bie);
            } catch (InitializationException ie) {
               LOG.error("Could not initialize sink instance " + sink.getId() + ". Reason: " + ie.getMessage(), ie);
               throw new ConfigurationException(ie);
            }
         }
      }
   }

   public void processEventlets() throws ConfigurationException {
      for (Eventlet eventlet : cfgHandler.getEventlets()) {
         final String eventletId = eventlet.getId();

         if (hasListenerBinding(eventletId) && !containerContext.hasEventlet(eventletId)) {
            try {
               containerContext.registerEventlet(eventlet.getId(), constructEventlet(eventlet.getType(), eventlet.getHref()));
            } catch (ReferenceInstantiationException bie) {
               LOG.error("Could not create eventlet instance " + eventlet.getId() + ". Reason: " + bie.getMessage(), bie);
            }

            try {
               final InstanceContext<AtomEventlet> eventletCtx = constructEventlet(eventlet.getType(), eventlet.getHref());
               eventletCtx.environment().stepInto();

               try {
                  eventletCtx.instance().init(new TaskContextImpl(parametersToMap(eventlet.getParameters()), services, tasker));
               } finally {
                  eventletCtx.environment().stepOut();
               }

               containerContext.registerEventlet(eventlet.getId(), eventletCtx);
            } catch (ReferenceInstantiationException bie) {
               LOG.error("Could not create eventlet instance " + eventlet.getId() + ". Reason: " + bie.getMessage(), bie);
               throw new ConfigurationException(bie);
            } catch (InitializationException ie) {
               LOG.error("Could not initialize eventlet instance " + eventlet.getId() + ". Reason: " + ie.getMessage(), ie);
               throw new ConfigurationException(ie);
            }
         }
      }
   }
}
