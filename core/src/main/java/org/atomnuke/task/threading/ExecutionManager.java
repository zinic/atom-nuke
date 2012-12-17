package org.atomnuke.task.threading;

import java.util.concurrent.Future;
import org.atomnuke.service.ServiceContext;
import org.atomnuke.lifecycle.ResourceLifeCycle;

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

   Future submit(Runnable r);

   TrackedFuture submitTracked(long handle, Runnable r);

   boolean submitted(long handle);

   State state();
}
