package org.atomnuke.task.manager;

import java.util.List;
import org.atomnuke.task.Tasker;
import org.atomnuke.util.TimeValue;

/**
 *
 * @author zinic
 */
public interface TaskManager extends Tasker {

   void destroy();

   List<ManagedTask> managedTasks();

   TimeValue scheduleTasks();
}
