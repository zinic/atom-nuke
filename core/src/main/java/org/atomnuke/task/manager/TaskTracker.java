package org.atomnuke.task.manager;

import java.util.List;
import java.util.UUID;
import org.atomnuke.task.ManagedTask;

/**
 *
 * @author zinic
 */
public interface TaskTracker {

   /**
    * Looks up a ManagedTask by id.
    *
    * @param taskId the task ID to look up.
    * @return the managed instance of the task, null otherwise.
    */
   ManagedTask findTask(UUID taskId);

   List<ManagedTask> activeTasks();

   void registerTask(ManagedTask managedTask);
}
