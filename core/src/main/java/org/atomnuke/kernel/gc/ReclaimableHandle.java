package org.atomnuke.kernel.gc;

import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.task.TaskHandle;
import org.atomnuke.util.lifecycle.Reclaimable;
import org.atomnuke.util.lifecycle.operation.ReclaimOperation;

/**
 *
 * @author zinic
 */
public class ReclaimableHandle {

   private final InstanceContext<Reclaimable> reclaimableInstance;
   private final TaskHandle taskHandle;

   public ReclaimableHandle(InstanceContext<Reclaimable> reclaimableInstance, TaskHandle taskHandle) {
      this.reclaimableInstance = reclaimableInstance;
      this.taskHandle = taskHandle;
   }

   public boolean shouldReclaim() {
      return taskHandle.cancellationRemote().canceled();
   }

   public void reclaim() {
      reclaimableInstance.perform(ReclaimOperation.instance());
   }
}
