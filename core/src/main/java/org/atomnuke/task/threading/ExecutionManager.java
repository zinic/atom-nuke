package org.atomnuke.task.threading;

import org.atomnuke.service.lifecycle.ServiceLifeCycle;
import org.atomnuke.task.TaskHandle;

/**
 *
 * @author zinic
 */
public interface ExecutionManager extends ServiceLifeCycle {

   public enum State {

      NEW,
      STARTING,
      READY,
      DRAINING,
      STOPPING,
      DESTROYED
   }

   TaskFuture submit(TaskHandle handle, Runnable r);

   boolean submitted(TaskHandle handle);

   State state();
}
