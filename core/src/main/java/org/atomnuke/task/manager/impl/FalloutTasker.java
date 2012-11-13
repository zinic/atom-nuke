package org.atomnuke.task.manager.impl;

import org.atomnuke.task.manager.Tasker;
import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.plugin.operation.ComplexOperation;
import org.atomnuke.plugin.operation.OperationFailureException;
import org.atomnuke.service.gc.ReclamationHandler;
import org.atomnuke.task.PollingTaskHandle;
import org.atomnuke.task.TaskHandle;
import org.atomnuke.task.impl.PollingTask;
import org.atomnuke.task.impl.PollingTaskHandleImpl;
import org.atomnuke.task.impl.QueuedRunTask;
import org.atomnuke.task.impl.TaskHandleImpl;
import org.atomnuke.task.manager.TaskTracker;
import org.atomnuke.util.TimeValue;
import org.atomnuke.util.lifecycle.runnable.ReclaimableTask;
import org.atomnuke.util.remote.AtomicCancellationRemote;
import org.atomnuke.util.remote.CancellationRemote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class FalloutTasker implements Tasker {

   private final Logger LOG = LoggerFactory.getLogger(FalloutTasker.class);

   private final ComplexOperation<ReclaimableTask, TaskHandle> ENLISTED_OPERATION = new ComplexOperation<ReclaimableTask, TaskHandle>() {

      @Override
      public void perform(ReclaimableTask instance, TaskHandle argument) throws OperationFailureException {
         instance.enlisted(argument);
      }
   };

   private final ReclamationHandler reclamationHandler;
   private final TaskTracker taskTracker;

   public FalloutTasker(TaskTracker taskTracker, ReclamationHandler reclamationHandler) {
      this.taskTracker = taskTracker;
      this.reclamationHandler = reclamationHandler;
   }

   @Override
   public TaskHandle queueTask(InstanceContext<? extends ReclaimableTask> runnableContext) {
      final CancellationRemote cancellationRemote = new AtomicCancellationRemote();
      final TaskHandle newHandle = new TaskHandleImpl(true, cancellationRemote);

      try {
         ((InstanceContext<ReclaimableTask>)runnableContext).<TaskHandle>perform(ENLISTED_OPERATION, newHandle);
         taskTracker.add(new QueuedRunTask(runnableContext, newHandle));
      } catch(OperationFailureException ofe) {
         LOG.error(ofe.getMessage(), ofe);
         cancellationRemote.cancel();
      }

      return newHandle;
   }

   @Override
   public TaskHandle pollTask(InstanceContext<? extends ReclaimableTask> instanceContext, TimeValue pollingInterval) {
      final CancellationRemote cancellationRemote = reclamationHandler.watch(instanceContext);
      final PollingTaskHandle newHandle = new PollingTaskHandleImpl(false, pollingInterval, cancellationRemote);

      taskTracker.add(new PollingTask(instanceContext, newHandle));

      return newHandle;
   }
}
