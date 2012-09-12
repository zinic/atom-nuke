package org.atomnuke.task.manager;

import java.util.List;
import org.atomnuke.task.Tasker;
import org.atomnuke.task.lifecycle.InitializationException;
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
    * Initializes the TaskManager. This should only be called once.
    *
    * @throws InitializationException
    */
   void init();

   /**
    * Destroys the TaskManager. This should only be called once.
    */
   void destroy();

   /**
    * Returns a copy of all of the tasks currently registered to the Nuke
    * scheduler.
    *
    * @return a list of the currently scheduled managed tasks
    */
   List<ManagedTask> managedTasks();

   /**
    * Advances the scheduler and dispatches tasks to the execution queue.
    *
    * @return the suggested time interval for scheduler sleeping.
    */
   TimeValue scheduleTasks();
}
