package org.atomnuke.task.manager;

import java.util.List;
import org.atomnuke.task.ManagedTask;
import org.atomnuke.util.TimeValue;

/**
 *
 * @author zinic
 */
public interface TaskTracker {

   /**
    * Checks to see if any of the registered tasks wants to opt in to the execution pool.
    * 
    * @param now
    * @return 
    */
   boolean hasTasksToSchedule(TimeValue now);
   
   /**
    * Returns an unmodifiable list of all the managed tasks.
    * 
    * @return 
    */
   List<ManagedTask> activeTasks();

   /**
    * Safely adds a task to the tracker.
    * 
    * @param task 
    */
   void enlistAction(ManagedTask task);

   /**
    * A TaskTracker is considered active it can accept tasks.
    *
    * @return
    */
   boolean active();
}
