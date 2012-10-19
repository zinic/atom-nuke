package org.atomnuke.task.threading;

import org.atomnuke.service.context.ServiceContext;
import org.atomnuke.task.TaskHandle;
import org.atomnuke.util.lifecycle.ResourceLifeCycle;

/**
 *
 * @author zinic
 */
public interface ExecutionManager extends ResourceLifeCycle<ServiceContext> {

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
