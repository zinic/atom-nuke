package org.atomnuke.fallout.context;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.atomnuke.config.model.Binding;
import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.sink.AtomSink;
import org.atomnuke.sink.eps.EventletChainSink;
import org.atomnuke.sink.eps.eventlet.AtomEventlet;
import org.atomnuke.task.atom.AtomTask;
import org.atomnuke.util.config.ConfigurationException;
import org.atomnuke.util.remote.CancellationRemote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class ContainerContext {

   private static final Logger LOG = LoggerFactory.getLogger(ContainerContext.class);

   private final Map<String, InstanceContext<AtomSink>> sinks;
   private final Map<String, InstanceContext<AtomEventlet>> eventlets;
   private final Map<String, InstanceContext<EventletChainSink>> relays;
   private final Map<String, CancellationRemote> cancellationRemotes;
   private final Map<String, AtomTask> tasks;
   private final Map<String, Binding> bindings;

   public ContainerContext() {
      sinks = new HashMap<String, InstanceContext<AtomSink>>();
      eventlets = new HashMap<String, InstanceContext<AtomEventlet>>();
      relays = new HashMap<String, InstanceContext<EventletChainSink>>();

      cancellationRemotes = new HashMap<String, CancellationRemote>();
      tasks = new HashMap<String, AtomTask>();
      bindings = new HashMap<String, Binding>();
   }

   public boolean hasRelay(String name) {
      return relays.containsKey(name);
   }

   public boolean hasSink(String name) {
      return sinks.containsKey(name);
   }

   public boolean hasEventlet(String name) {
      return eventlets.containsKey(name);
   }

   public boolean hasTask(String name) {
      return tasks.containsKey(name);
   }

   public void registerRelay(String name, InstanceContext<EventletChainSink> instanceCtx) {
      LOG.info("Registering relay: " + name);

      relays.put(name, instanceCtx);
   }

   public void registerSink(String name, InstanceContext<AtomSink> instanceCtx) {
      LOG.info("Registering Sink: " + name);

      sinks.put(name, instanceCtx);
   }

   public void registerEventlet(String name, InstanceContext<AtomEventlet> instanceCtx) {
      LOG.info("Registering eventlet: " + name);

      eventlets.put(name, instanceCtx);
   }

   public void registerSource(String name, AtomTask task) {
      LOG.info("Registering source: " + name);

      tasks.put(name, task);
      cancellationRemotes.put(name, task.handle().cancellationRemote());
   }

   public void process(List<Binding> bindingsToMerge) throws ConfigurationException {
      final Set<String> bindingsToBreak = new HashSet<String>(bindings.keySet());
      final Set<Binding> bindingsToAdd = new HashSet<Binding>();

      for (Binding binding : bindingsToMerge) {
         if (!bindingsToBreak.remove(binding.getId())) {
            bindingsToAdd.add(binding);
         }
      }

      for (Binding binding : bindingsToAdd) {
         bind(binding);
      }

      for (String breakId : bindingsToBreak) {
         LOG.info("Breaking binding: " + breakId);

         bindings.remove(breakId);
      }

      garbageCollect();
   }

   private boolean hasSourceBindingFor(String id) {
      for (Binding binding : bindings.values()) {
         if (binding.getSource().equals(id)) {
            return true;
         }
      }

      return false;
   }

   private boolean hasSinkBindingFor(String id) {
      for (Binding binding : bindings.values()) {
         if (binding.getSink().equals(id)) {
            return true;
         }
      }

      return false;
   }

   private void garbageCollect() {
      final List<CancellationRemote> garbageQueue = new LinkedList<CancellationRemote>();

      for (String id : new HashSet<String>(tasks.keySet())) {
         if (!hasSourceBindingFor(id)) {
            LOG.info("Garbage collecting task: " + id);

            tasks.remove(id);
            garbageQueue.add(cancellationRemotes.remove(id));
         }
      }

      for (String id : new HashSet<String>(sinks.keySet())) {
         if (!hasSinkBindingFor(id)) {
            LOG.info("Garbage collecting Sink: " + id);

            eventlets.remove(id);
            garbageQueue.add(cancellationRemotes.remove(id));
         }
      }

      for (String id : new HashSet<String>(relays.keySet())) {
         if (!hasSourceBindingFor(id) && !hasSinkBindingFor(id)) {
            LOG.info("Garbage collecting relay: " + id);

            eventlets.remove(id);
            garbageQueue.add(cancellationRemotes.remove(id));
         }
      }

      for (String id : new HashSet<String>(eventlets.keySet())) {
         if (!hasSinkBindingFor(id)) {
            LOG.info("Garbage collecting eventlet: " + id);

            eventlets.remove(id);
            garbageQueue.add(cancellationRemotes.remove(id));
         }
      }

      for (CancellationRemote cancellationRemote : garbageQueue) {
         // TODO: Refactor this so there aren't any nulls
         if (cancellationRemote != null) {
            cancellationRemote.cancel();
         }
      }
   }

   private void bind(Binding binding) throws ConfigurationException {
      LOG.info("Binding source " + binding.getSource() + " to sink " + binding.getSink());

      final AtomTask source = tasks.get(binding.getSource());

      if (source != null) {
         bind(source, binding);
      } else {
         final InstanceContext<EventletChainSink> relayContext = relays.get(binding.getSource());

         if (relayContext != null) {
            bind(relayContext.instance(), binding);
         } else {
            throw new ConfigurationException("Unable to locate source or relay, " + binding.getSource());
         }
      }
   }

   private InstanceContext<? extends AtomSink> findAtomSink(String id) {
      final InstanceContext<AtomSink> SinkContext = sinks.get(id);
      return SinkContext != null ? SinkContext : relays.get(id);
   }

   private void bind(AtomTask source, Binding binding) throws ConfigurationException {
      final InstanceContext<? extends AtomSink> SinkCtx = findAtomSink(binding.getSink());

      if (SinkCtx == null) {
         throw new ConfigurationException("Unable to locate Sink or realy, " + binding.getSink() + ".");
      }

      cancellationRemotes.put(binding.getSink(), source.addSink(SinkCtx));
      bindings.put(binding.getId(), binding);
   }

   private void bind(EventletChainSink source, Binding binding) throws ConfigurationException {
      final InstanceContext<? extends AtomEventlet> eventletCtx = eventlets.get(binding.getSink());

      if (eventletCtx == null) {
         throw new ConfigurationException("Unable to locate eventlet, " + binding.getSink() + ".");
      }

      cancellationRemotes.put(binding.getSink(), source.enlistHandler(eventletCtx));
      bindings.put(binding.getId(), binding);
   }
}
