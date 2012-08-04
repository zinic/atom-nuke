package org.atomnuke.listener.manager;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import org.atomnuke.listener.AtomListener;
import org.atomnuke.listener.ReentrantAtomListener;
import org.atomnuke.listener.RegisteredListener;
import org.atomnuke.util.remote.AtomicCancellationRemote;

/**
 *
 * @author zinic
 */
public class ListenerManagerImpl implements ListenerManager {

   private final List<RegisteredListener> listeners;
   private final AtomicBoolean reentrant;

   public ListenerManagerImpl() {
      listeners = new LinkedList<RegisteredListener>();
      reentrant = new AtomicBoolean(true);
   }

   @Override
   public synchronized boolean hasListeners() {
      return !listeners.isEmpty();
   }

   @Override
   public synchronized List<RegisteredListener> listeners() {
      for (Iterator<RegisteredListener> registeredListenerItr = listeners.iterator(); registeredListenerItr.hasNext();) {
         final RegisteredListener registeredListener = registeredListenerItr.next();

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
   public synchronized void addListener(AtomListener atomListener) {
      if (!(atomListener instanceof ReentrantAtomListener)) {
         reentrant.set(false);
      }

      listeners.add(new RegisteredListener(new AtomicCancellationRemote(), atomListener));
   }
}
