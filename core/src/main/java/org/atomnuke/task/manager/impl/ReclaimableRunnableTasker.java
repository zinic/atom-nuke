package org.atomnuke.task.manager.impl;

import org.atomnuke.task.manager.Tasker;
import java.util.UUID;
import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.plugin.InstanceContextImpl;
import org.atomnuke.plugin.env.NopInstanceEnvironment;
import org.atomnuke.service.gc.ReclaimationHandler;
import org.atomnuke.task.ReclaimableRunnable;
import org.atomnuke.task.TaskHandle;
import org.atomnuke.task.impl.EnvAwareManagedRunTask;
import org.atomnuke.task.impl.TaskHandleImpl;
import org.atomnuke.task.manager.TaskTracker;
import org.atomnuke.util.TimeValue;
import org.atomnuke.util.remote.CancellationRemote;

/**
 *
 * @author zinic
 */
public class ReclaimableRunnableTasker implements Tasker {

   private final ReclaimationHandler reclaimationHandler;
   private final TaskTracker taskTracker;

   public ReclaimableRunnableTasker(TaskTracker taskTracker, ReclaimationHandler reclaimationHandler) {
      this.taskTracker = taskTracker;
      this.reclaimationHandler = reclaimationHandler;
   }

   @Override
   public TaskHandle task(ReclaimableRunnable runnable, TimeValue pollingInterval) {
      return task(new InstanceContextImpl<ReclaimableRunnable>(NopInstanceEnvironment.getInstance(), runnable), pollingInterval);
   }

   @Override
   public TaskHandle task(InstanceContext<? extends ReclaimableRunnable> instanceContext, TimeValue pollingInterval) {
      final CancellationRemote cancellationRemote = reclaimationHandler.watch(instanceContext);
      final TaskHandle taskHandle = new TaskHandleImpl(cancellationRemote, pollingInterval, UUID.randomUUID());

      taskTracker.add(new EnvAwareManagedRunTask(instanceContext, taskHandle));

      return taskHandle;
   }
}
