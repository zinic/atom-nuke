package org.atomnuke.container.context;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.atomnuke.config.model.Binding;
import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.listener.AtomListener;
import org.atomnuke.listener.eps.EventletRelay;
import org.atomnuke.listener.eps.eventlet.AtomEventlet;
import org.atomnuke.task.Task;
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

   private final Map<String, InstanceContext<AtomListener>> listeners;
   private final Map<String, InstanceContext<AtomEventlet>> eventlets;
   private final Map<String, InstanceContext<EventletRelay>> relays;
   private final Map<String, CancellationRemote> cancellationRemotes;
   private final Map<String, Task> tasks;
   private final Map<String, Binding> bindings;

   public ContainerContext() {
      listeners = new HashMap<String, InstanceContext<AtomListener>>();
      eventlets = new HashMap<String, InstanceContext<AtomEventlet>>();
      relays = new HashMap<String, InstanceContext<EventletRelay>>();

      cancellationRemotes = new HashMap<String, CancellationRemote>();
      tasks = new HashMap<String, Task>();
      bindings = new HashMap<String, Binding>();
   }

   public boolean hasRelay(String name) {
      return relays.containsKey(name);
   }

   public boolean hasSink(String name) {
      return listeners.containsKey(name);
   }

   public boolean hasEventlet(String name) {
      return eventlets.containsKey(name);
   }

   public boolean hasTask(String name) {
      return tasks.containsKey(name);
   }

   public void registerRelay(String name, InstanceContext<EventletRelay> instanceCtx) {
      LOG.info("Registering relay: " + name);

      relays.put(name, instanceCtx);
   }

   public void registerSink(String name, InstanceContext<AtomListener> instanceCtx) {
      LOG.info("Registering listeners: " + name);

      listeners.put(name, instanceCtx);
   }

   public void registerEventlet(String name, InstanceContext<AtomEventlet> instanceCtx) {
      LOG.info("Registering eventlet: " + name);

      eventlets.put(name, instanceCtx);
   }

   public void registerSource(String name, Task task) {
      LOG.info("Registering source: " + name);

      tasks.put(name, task);
      cancellationRemotes.put(name, task.cancellationRemote());
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

   private boolean hasTargetBindingFor(String id) {
      for (Binding binding : bindings.values()) {
         if (binding.getTarget().equals(id)) {
            return true;
         }
      }

      return false;
   }

   private boolean hasRecieverBindingFor(String id) {
      for (Binding binding : bindings.values()) {
         if (binding.getReceiver().equals(id)) {
            return true;
         }
      }

      return false;
   }

   private void garbageCollect() {
      final List<CancellationRemote> garbageQueue = new LinkedList<CancellationRemote>();

      for (String id : new HashSet<String>(tasks.keySet())) {
         if (!hasTargetBindingFor(id)) {
            LOG.info("Garbage collecting task: " + id);

            tasks.remove(id);
            garbageQueue.add(cancellationRemotes.remove(id));
         }
      }

      for (String id : new HashSet<String>(listeners.keySet())) {
         if (!hasRecieverBindingFor(id)) {
            LOG.info("Garbage collecting listener: " + id);

            eventlets.remove(id);
            garbageQueue.add(cancellationRemotes.remove(id));
         }
      }

      for (String id : new HashSet<String>(relays.keySet())) {
         if (!hasTargetBindingFor(id) && !hasRecieverBindingFor(id)) {
            LOG.info("Garbage collecting relay: " + id);

            eventlets.remove(id);
            garbageQueue.add(cancellationRemotes.remove(id));
         }
      }

      for (String id : new HashSet<String>(eventlets.keySet())) {
         if (!hasRecieverBindingFor(id)) {
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
      LOG.info("Binding " + binding.getReceiver() + " to source " + binding.getTarget());

      final Task source = tasks.get(binding.getTarget());

      if (source != null) {
         bind(source, binding);
      } else {
         final InstanceContext<EventletRelay> relayContext = relays.get(binding.getTarget());

         if (relayContext != null) {
            bind(relayContext.instance(), binding);
         } else {
            throw new ConfigurationException("Unable to locate source or relay, " + binding.getTarget());
         }
      }
   }

   private InstanceContext<? extends AtomListener> findAtomListener(String id) {
      final InstanceContext<AtomListener> listenerContext = listeners.get(id);
      return listenerContext != null ? listenerContext : relays.get(id);
   }

   private void bind(Task source, Binding binding) throws ConfigurationException {
      final InstanceContext<? extends AtomListener> listenerCtx = findAtomListener(binding.getReceiver());

      if (listenerCtx == null) {
         throw new ConfigurationException("Unable to locate listener or realy, " + binding.getReceiver() + ".");
      }

      cancellationRemotes.put(binding.getReceiver(), source.addListener(listenerCtx.environment(), listenerCtx.instance()));
      bindings.put(binding.getId(), binding);
   }

   private void bind(EventletRelay source, Binding binding) throws ConfigurationException {
      final InstanceContext<? extends AtomEventlet> eventletCtx = eventlets.get(binding.getReceiver());

      if (eventletCtx == null) {
         throw new ConfigurationException("Unable to locate eventlet, " + binding.getReceiver() + ".");
      }

      cancellationRemotes.put(binding.getReceiver(), source.enlistHandlerContext(eventletCtx.environment(), eventletCtx.instance()));
      bindings.put(binding.getId(), binding);
   }
}
