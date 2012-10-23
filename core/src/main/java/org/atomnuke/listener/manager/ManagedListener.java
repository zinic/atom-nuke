package org.atomnuke.listener.manager;

import java.util.UUID;
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
   private final UUID taskId;

   public ManagedListener(InstanceContext<? extends AtomListener> listenerContext, CancellationRemote cancellationRemote, UUID taskId) {
      this.listenerContext = (InstanceContext<AtomListener>) listenerContext;
      this.cancellationRemote = cancellationRemote;
      this.taskId = taskId;
   }

   public CancellationRemote cancellationRemote() {
      return cancellationRemote;
   }

   public UUID taskId() {
      return taskId;
   }

   public InstanceContext<AtomListener> listenerContext() {
      return listenerContext;
   }
}
