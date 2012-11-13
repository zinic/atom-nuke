package org.atomnuke.fallout.service.gc;

import org.atomnuke.service.gc.ReclamationHandler;
import org.atomnuke.util.lifecycle.runnable.ReclaimableTaskPartial;

/**
 *
 * @author zinic
 */
public class GarbageCollectionTask extends ReclaimableTaskPartial {

   private final ReclamationHandler reclamationHandler;

   public GarbageCollectionTask(ReclamationHandler reclamationHandler) {
      this.reclamationHandler = reclamationHandler;
   }

   @Override
   public void run() {
      reclamationHandler.garbageCollect();
   }
}
