package org.atomnuke.task.manager.service;

import org.atomnuke.task.manager.TaskTracker;
import org.atomnuke.task.manager.Tasker;

/**
 *
 * @author zinic
 */
public interface TaskingModule {

   TaskTracker taskTracker();

   Tasker tasker();
}
