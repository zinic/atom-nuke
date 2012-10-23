package org.atomnuke.task.threading;

import java.util.UUID;
import org.atomnuke.service.context.ServiceContext;
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

   ExecutionFuture submit(UUID handle, Runnable r);

   boolean submitted(UUID taskId);

   State state();
}
