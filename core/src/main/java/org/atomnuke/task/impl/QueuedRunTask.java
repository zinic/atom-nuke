package org.atomnuke.task.impl;

import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.task.TaskHandle;
import org.atomnuke.util.TimeValue;

/**
 *
 * @author zinic
 */
public class QueuedRunTask extends EnvironmentAwareManagedTask<TaskHandle> {

   private final TimeValue runtime;

   public QueuedRunTask(InstanceContext<? extends Runnable> runnableCtx, TaskHandle taskHandle) {
      super((InstanceContext<Runnable>) runnableCtx, taskHandle);

      runtime = TimeValue.now();
   }

   @Override
   public TimeValue nextRunTime() {
      return runtime;
   }

   @Override
   public void scheduleNext() {
      handle().cancellationRemote().cancel();
   }
}
