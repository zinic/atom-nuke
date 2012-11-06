package org.atomnuke.fallout.service.tasker;

import org.atomnuke.task.manager.TaskTracker;
import org.atomnuke.task.manager.Tasker;
import org.atomnuke.task.manager.service.TaskingService;

/**
 *
 * @author zinic
 */
public class FalloutTaskingService implements TaskingService {

   private final TaskTracker taskTracker;
   private final Tasker tasker;

   public FalloutTaskingService(TaskTracker taskTracker, Tasker tasker) {
      this.taskTracker = taskTracker;
      this.tasker = tasker;
   }

   @Override
   public TaskTracker taskTracker() {
      return taskTracker;
   }

   @Override
   public Tasker tasker() {
      return tasker;
   }
}
