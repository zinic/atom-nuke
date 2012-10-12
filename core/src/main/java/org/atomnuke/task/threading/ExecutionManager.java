package org.atomnuke.task.threading;

import java.util.UUID;
import org.atomnuke.service.ServiceLifeCycle;

/**
 *
 * @author zinic
 */
public interface ExecutionManager extends ServiceLifeCycle {

   public enum State {

      NEW,
      STARTING,
      OK,
      DRAINING,
      STOPPING,
      DESTROYED
   }

   TaskFuture submit(Runnable r);

   TaskFuture submit(UUID id, Runnable r);

   boolean submitted(UUID id);

   State state();
}
