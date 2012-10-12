package org.atomnuke.task.manager;

import java.util.UUID;
import org.atomnuke.task.Tasker;
import org.atomnuke.util.TimeValue;

/**
 * The TaskManager interface abstracts the complexity of of adding tasks to the
 * Nuke scheduler. It follows its own life-cycle, having init and destroy
 * methods.
 *
 * The task manager is responsible for scheduling tasks into the execution queue
 * and recommend to the scheduler the next wake time for efficient polling.
 *
 * @author zinic
 */
public interface TaskManager extends Tasker {

   /**
    * Looks up a ManagedTask by id.
    *
    * @param taskId the task ID to look up.
    * @return the managed instance of the task, null otherwise.
    */
   ManagedTask findTask(UUID taskId);

   /**
    * Advances the scheduler and dispatches tasks to the execution queue.
    *
    * @return the suggested time interval for scheduler sleeping.
    */
   TimeValue scheduleTasks();

   void init();

   void destroy();
}
