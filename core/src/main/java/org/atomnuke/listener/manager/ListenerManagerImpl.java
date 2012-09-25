package org.atomnuke.listener.manager;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import org.atomnuke.plugin.InstanceEnvironment;
import org.atomnuke.listener.AtomListener;
import org.atomnuke.listener.ReentrantAtomListener;
import org.atomnuke.util.remote.AtomicCancellationRemote;
import org.atomnuke.util.remote.CancellationRemote;

/**
 *
 * @author zinic
 */
public class ListenerManagerImpl implements ListenerManager {

   private final List<ManagedListener> listeners;
   private final AtomicBoolean reentrant;

   public ListenerManagerImpl() {
      listeners = new LinkedList<ManagedListener>();
      reentrant = new AtomicBoolean(true);
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
   public boolean isReentrant() {
      return reentrant.get();
   }

   @Override
   public synchronized CancellationRemote addListener(InstanceEnvironment<? extends AtomListener> atomListenerContext) {
      if (!(atomListenerContext.getInstance() instanceof ReentrantAtomListener)) {
         reentrant.set(false);
      }

      final CancellationRemote cancellationRemote = new AtomicCancellationRemote();
      listeners.add(new ManagedListener(cancellationRemote, atomListenerContext));

      return cancellationRemote;
   }
}
