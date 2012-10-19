package org.atomnuke.task.manager;

import java.util.List;
import org.atomnuke.task.manager.ManagedTask;

/**
 *
 * @author zinic
 */
public interface TaskTracker {

   List<ManagedTask> activeTasks();

   void addTask(ManagedTask managedTask);
}
