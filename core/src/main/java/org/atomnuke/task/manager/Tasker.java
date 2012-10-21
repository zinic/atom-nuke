package org.atomnuke.task.manager;

import java.util.List;
import java.util.UUID;
import org.atomnuke.service.Service;
import org.atomnuke.task.ManagedTask;
import org.atomnuke.task.TaskHandle;
import org.atomnuke.util.TimeValue;

/**
 *
 * @author zinic
 */
public interface Tasker {

   /**
    * Looks up a ManagedTask by id.
    *
    * @param taskId the task ID to look up.
    * @return the managed instance of the task, null otherwise.
    */
   ManagedTask findTask(UUID taskId);

   List<ManagedTask> activeTasks();

   TaskHandle task(Runnable runnable, TimeValue taskInterval);

   void task(ManagedTask managedTask);
}
