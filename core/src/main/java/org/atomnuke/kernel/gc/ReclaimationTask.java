package org.atomnuke.kernel.gc;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.atomnuke.task.TaskHandle;
import org.atomnuke.task.impl.AbstractManagedTask;

/**
 *
 * @author zinic
 */
public class ReclaimationTask extends AbstractManagedTask {

   private final List<ReclaimableHandle> reclimationHandles;

   public ReclaimationTask(TaskHandle taskHandle) {
      super(taskHandle);

      reclimationHandles = new LinkedList<ReclaimableHandle>();
   }

   @Override
   public synchronized void run() {
      for (Iterator<ReclaimableHandle> reclaimableItr = reclimationHandles.iterator(); reclaimableItr.hasNext();) {
         final ReclaimableHandle reclaimableHandle = reclaimableItr.next();

         if (reclaimableHandle.shouldReclaim()) {
            reclaimableHandle.reclaim();
            reclaimableItr.remove();
         }
      }
   }
}
