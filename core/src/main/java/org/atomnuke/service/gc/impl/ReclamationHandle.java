package org.atomnuke.service.gc.impl;

import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.util.lifecycle.Reclaimable;
import org.atomnuke.util.lifecycle.operation.ReclaimOperation;
import org.atomnuke.util.remote.CancellationRemote;

/**
 *
 * @author zinic
 */
public class ReclamationHandle {

   private final InstanceContext<Reclaimable> reclaimableContext;
   private final CancellationRemote cancellationRemote;

   public ReclamationHandle(InstanceContext<Reclaimable> reclaimableContext, CancellationRemote cancellationRemote) {
      this.reclaimableContext = reclaimableContext;
      this.cancellationRemote = cancellationRemote;
   }

   public boolean shouldReclaim() {
      return cancellationRemote.canceled();
   }

   public void reclaim() {
      cancellationRemote.cancel();
      reclaimableContext.perform(ReclaimOperation.instance());
   }
}
