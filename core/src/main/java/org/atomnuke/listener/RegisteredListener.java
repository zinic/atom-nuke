package org.atomnuke.listener;

import org.atomnuke.context.InstanceContext;
import org.atomnuke.util.remote.CancellationRemote;

/**
 *
 * @author zinic
 */
public class RegisteredListener {

   private final CancellationRemote cancellationRemote;
   private final InstanceContext<? extends AtomListener> listenerContext;

   public RegisteredListener(CancellationRemote cancellationRemote, InstanceContext<? extends AtomListener> listenerContext) {
      this.cancellationRemote = cancellationRemote;
      this.listenerContext = listenerContext;
   }

   public CancellationRemote cancellationRemote() {
      return cancellationRemote;
   }

   public void cancel() {
      cancellationRemote.cancel();
   }

   public InstanceContext<? extends AtomListener> listenerContext() {
      return listenerContext;
   }
}
