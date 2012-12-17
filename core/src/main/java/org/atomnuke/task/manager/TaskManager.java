package org.atomnuke.task.manager;

import org.atomnuke.util.TimeValue;
import org.atomnuke.lifecycle.Reclaimable;

/**
 * The TaskManager interface abstracts the complexity of of adding tasks to the
 * Nuke scheduler.
 *
 * The task manager is responsible for scheduling tasks into the execution queue
 * and recommend to the scheduler the next wake time for efficient polling.
 *
 * @author zinic
 */
public interface TaskManager extends Reclaimable {

   public enum State {

      NEW,
      READY,
      DRAINING,
      DESTROYED
   }

   /**
    * Gets the state of the task manager.
    *
    * @return
    */
   State state();

   /**
    * Advances the scheduler and dispatches tasks to the execution queue.
    *
    * @return the suggested time interval for scheduler sleeping.
    */
   TimeValue scheduleTasks();
}
