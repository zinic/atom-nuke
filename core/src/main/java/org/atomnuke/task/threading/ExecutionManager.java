package org.atomnuke.task.threading;

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

   ExecutionFuture submit(long handle, Runnable r);

   boolean submitted(long handle);

   State state();
}
