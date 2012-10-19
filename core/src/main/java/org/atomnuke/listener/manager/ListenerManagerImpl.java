package org.atomnuke.listener.manager;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.listener.AtomListener;
import org.atomnuke.task.TaskHandle;
import org.atomnuke.util.remote.CancellationRemote;

/**
 *
 * @author zinic
 */
public class ListenerManagerImpl implements ListenerManager {

   private final List<ManagedListener> listeners;
   private final TaskHandle parentHandle;

   public ListenerManagerImpl(TaskHandle parentHandle) {
      this.parentHandle = parentHandle;

      listeners = new LinkedList<ManagedListener>();
   }

   @Override
   public synchronized boolean hasListeners() {
      return !listeners.isEmpty();
   }

   @Override
   public synchronized List<ManagedListener> listeners() {
      for (Iterator<ManagedListener> registeredListenerItr = listeners.iterator(); registeredListenerItr.hasNext();) {
         final ManagedListener registeredListener = registeredListenerItr.next();

         if (registeredListener.cancellationRemote().canceled()) {
            registeredListenerItr.remove();
         }
      }

      return Collections.unmodifiableList(listeners);
   }

   @Override
   public synchronized CancellationRemote addListener(InstanceContext<? extends AtomListener> atomListenerContext) {
      final ManagedListener newListener = new ManagedListener((InstanceContext<AtomListener>) atomListenerContext, parentHandle);
      listeners.add(newListener);

      return newListener.cancellationRemote();
   }
}
