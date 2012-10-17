package org.atomnuke.listener.manager;

import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.listener.AtomListener;
import org.atomnuke.util.remote.CancellationRemote;

/**
 *
 * @author zinic
 */
public class ManagedListener {

   private final InstanceContext<AtomListener> listenerContext;
   private final CancellationRemote cancellationRemote;

   public ManagedListener(CancellationRemote cancellationRemote, InstanceContext<AtomListener> listenerContext) {
      this.cancellationRemote = cancellationRemote;
      this.listenerContext = listenerContext;
   }

   public CancellationRemote cancellationRemote() {
      return cancellationRemote;
   }

   public InstanceContext<AtomListener> listenerContext() {
      return listenerContext;
   }
}
