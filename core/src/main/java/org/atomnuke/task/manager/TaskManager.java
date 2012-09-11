package org.atomnuke.task.manager;

import java.util.List;
import org.atomnuke.task.Tasker;
import org.atomnuke.task.lifecycle.InitializationException;
import org.atomnuke.util.TimeValue;

/**
 *
 * @author zinic
 */
public interface TaskManager extends Tasker {

   void init() throws InitializationException;

   void destroy();

   List<ManagedTask> managedTasks();

   TimeValue scheduleTasks();
}
