package org.atomnuke.task.polling;

import org.atomnuke.task.TaskHandle;

/**
 *
 * @author zinic
 */
public abstract class AbstractPollingController implements PollingController {

   private final TaskHandle taskHandle;

   public AbstractPollingController(TaskHandle taskHandle) {
      this.taskHandle = taskHandle;
   }

   protected TaskHandle taskHandle() {
      return taskHandle;
   }
}
