package org.atomnuke.task.polling;

/**
 * A task future represents operations that may happen during the task's life-cycle.
 *
 * @author zinic
 */
public interface TaskFuture {

   /**
    * This sets the runnable delegate to run once the task has completed execution.
    * @param runnable 
    */
   void onTaskCompletion(Runnable runnable);
}
