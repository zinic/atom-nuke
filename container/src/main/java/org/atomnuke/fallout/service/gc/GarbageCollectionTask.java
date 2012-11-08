package org.atomnuke.fallout.service.gc;

import org.atomnuke.service.gc.ReclamationHandler;
import org.atomnuke.util.lifecycle.runnable.ReclaimableTask;

/**
 *
 * @author zinic
 */
public class GarbageCollectionTask implements ReclaimableTask {

   private final ReclamationHandler reclamationHandler;

   public GarbageCollectionTask(ReclamationHandler reclamationHandler) {
      this.reclamationHandler = reclamationHandler;
   }

   @Override
   public void destroy() {
   }

   @Override
   public void run() {
      reclamationHandler.garbageCollect();
   }
}
