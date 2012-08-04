package org.atomnuke.listener;

import org.atomnuke.util.remote.CancellationRemote;

/**
 *
 * @author zinic
 */
public class RegisteredListener {

   private final CancellationRemote cancellationRemote;
   private final AtomListener listener;

   public RegisteredListener(CancellationRemote cancellationRemote, AtomListener listener) {
      this.cancellationRemote = cancellationRemote;
      this.listener = listener;
   }

   public CancellationRemote cancellationRemote() {
      return cancellationRemote;
   }

   public void cancel() {
      cancellationRemote.cancel();
   }

   public AtomListener listener() {
      return listener;
   }
}
