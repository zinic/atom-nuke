package org.atomnuke.task.impl;

import org.atomnuke.task.ManagedTask;
import org.atomnuke.task.TaskHandle;
import org.atomnuke.util.TimeValue;

/**
 *
 * @author zinic
 */
public abstract class AbstractManagedTask implements ManagedTask {

   private final TaskHandle taskHandle;
   private TimeValue timestamp;

   public AbstractManagedTask(TaskHandle taskHandle) {
      this.taskHandle = taskHandle;

      timestamp = TimeValue.now();
   }

   @Override
   public final void scheduleNext() {
      timestamp = TimeValue.now();
   }

   @Override
   public final TimeValue nextPollTime() {
      return timestamp.add(handle().scheduleInterval());
   }

   @Override
   public final TaskHandle handle() {
      return taskHandle;
   }
}
