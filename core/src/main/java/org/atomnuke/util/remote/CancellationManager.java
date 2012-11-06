package org.atomnuke.util.remote;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.atomnuke.util.lifecycle.Reclaimable;

/**
 *
 * @author zinic
 */
public class CancellationManager implements Reclaimable {

   private final List<WeakReference<CancellationRemote>> cancellationRemotes;

   public CancellationManager() {
      cancellationRemotes = new LinkedList<WeakReference<CancellationRemote>>();
   }

   @Override
   public synchronized void destroy() {
      for (CancellationRemote remote : remotes()) {
         remote.cancel();
      }

      cancellationRemotes.clear();
   }

   public synchronized void add(CancellationRemote remote) {
      cancellationRemotes.add(new WeakReference<CancellationRemote>(remote));
   }

   private List<CancellationRemote> remotes() {
      final List<CancellationRemote> activeRemotes = new LinkedList<CancellationRemote>();

      for (Iterator<WeakReference<CancellationRemote>> cancellationRemoteItr = cancellationRemotes.iterator(); cancellationRemoteItr.hasNext();) {
         final CancellationRemote remoteReference = cancellationRemoteItr.next().get();

         if (remoteReference == null || remoteReference.canceled()) {
            cancellationRemoteItr.remove();
         } else {
            activeRemotes.add(remoteReference);
         }
      }

      return activeRemotes;
   }
}
