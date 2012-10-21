package org.atomnuke.listener.manager;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.listener.AtomListener;
import org.atomnuke.service.gc.ReclaimationHandler;
import org.atomnuke.task.TaskHandle;
import org.atomnuke.util.remote.CancellationRemote;

/**
 *
 * @author zinic
 */
public class ListenerManagerImpl implements ListenerManager {

   private final ReclaimationHandler reclaimationHandler;
   private final List<ManagedListener> listeners;
   private final TaskHandle parentHandle;

   public ListenerManagerImpl(ReclaimationHandler reclaimationHandler, TaskHandle parentHandle) {
      this.reclaimationHandler = reclaimationHandler;
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
      final CancellationRemote cancellationRemote = reclaimationHandler.watch(atomListenerContext);

      final ManagedListener newListener = new ManagedListener(cancellationRemote, (InstanceContext<AtomListener>) atomListenerContext, parentHandle);
      listeners.add(newListener);

      return cancellationRemote;
   }
}
