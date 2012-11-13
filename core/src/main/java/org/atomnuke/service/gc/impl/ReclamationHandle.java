package org.atomnuke.service.gc.impl;

import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.plugin.operation.OperationFailureException;
import org.atomnuke.lifecycle.Reclaimable;
import org.atomnuke.util.lifecycle.operation.ReclaimOperation;
import org.atomnuke.util.remote.CancellationRemote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class ReclamationHandle {

   private static final Logger LOG = LoggerFactory.getLogger(ReclamationHandle.class);

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

      try {
         reclaimableContext.perform(ReclaimOperation.instance());
      } catch (OperationFailureException ofe) {
         LOG.error("Failed to destroy reclamation context: " + reclaimableContext+ " - Reason: " + ofe.getMessage(), ofe);
      }
   }
}
