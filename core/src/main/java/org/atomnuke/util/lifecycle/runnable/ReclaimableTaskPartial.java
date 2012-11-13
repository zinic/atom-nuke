package org.atomnuke.util.lifecycle.runnable;

import org.atomnuke.task.TaskHandle;

/**
 *
 * @author zinic
 */
public abstract class ReclaimableTaskPartial implements ReclaimableTask {

   @Override
   public void enlisted(TaskHandle taskHandle) {
   }

   @Override
   public void destroy() {
   }
}
