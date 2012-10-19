package org.atomnuke.container.service.gc.impl;

import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.util.lifecycle.Reclaimable;
import org.atomnuke.util.lifecycle.operation.ReclaimOperation;
import org.atomnuke.util.remote.CancellationRemote;

/**
 *
 * @author zinic
 */
public class ReclaimationHandle {

   private final InstanceContext<Reclaimable> reclaimableContext;
   private final CancellationRemote cancellationRemote;

   public ReclaimationHandle(InstanceContext<Reclaimable> reclaimableContext, CancellationRemote cancellationRemote) {
      this.reclaimableContext = reclaimableContext;
      this.cancellationRemote = cancellationRemote;
   }

   public boolean shouldReclaim() {
      return cancellationRemote.canceled();
   }

   // TODO: Validate whether or not explicit calling of cancel is a good idea
   public void reclaim() {
      cancellationRemote.cancel();
      reclaimableContext.perform(ReclaimOperation.instance());
   }
}
