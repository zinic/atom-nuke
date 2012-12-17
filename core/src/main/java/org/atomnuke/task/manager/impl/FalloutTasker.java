package org.atomnuke.task.manager.impl;

import org.atomnuke.task.manager.Tasker;
import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.plugin.operation.ComplexOperation;
import org.atomnuke.plugin.operation.OperationFailureException;
import org.atomnuke.service.gc.ReclamationHandler;
import org.atomnuke.task.TaskHandle;
import org.atomnuke.task.atom.impl.EnvironmentAwareManagedTask;
import org.atomnuke.task.atom.impl.TaskHandleImpl;
import org.atomnuke.task.manager.TaskTracker;
import org.atomnuke.task.polling.PollingController;
import org.atomnuke.task.polling.TimeIntervalPollingController;
import org.atomnuke.task.threading.ExecutionManager;
import org.atomnuke.task.threading.InstanceContextRunnableWrapper;
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
   private final ExecutionManager executionManager;
   private final TaskTracker taskTracker;

   public FalloutTasker(ReclamationHandler reclamationHandler, ExecutionManager executionManager, TaskTracker taskTracker) {
      this.reclamationHandler = reclamationHandler;
      this.executionManager = executionManager;
      this.taskTracker = taskTracker;
   }

   
   @Override
   public TaskHandle queueTask(InstanceContext<? extends ReclaimableTask> instanceContext, PollingController pollingController) {
      final CancellationRemote cancellationRemote = new AtomicCancellationRemote();
      final TaskHandle newHandle = new TaskHandleImpl(false, cancellationRemote);

      try {
         addTask(instanceContext, newHandle, pollingController);
      } catch (OperationFailureException ofe) {
         cancellationRemote.cancel();
      }

      return newHandle;
   }

   @Override
   public void queueAction(InstanceContext<? extends ReclaimableTask> runnableContext) {
      executionManager.submit(new InstanceContextRunnableWrapper(runnableContext));
   }

   @Override
   public TaskHandle pollTask(InstanceContext<? extends ReclaimableTask> instanceContext, TimeValue pollingInterval) {
      final CancellationRemote cancellationRemote = reclamationHandler.watch(instanceContext);
      final TaskHandle newHandle = new TaskHandleImpl(false, cancellationRemote);

      try {
         ((InstanceContext<ReclaimableTask>) instanceContext).<TaskHandle>perform(ENLISTED_OPERATION, newHandle);

         taskTracker.enlistAction(new EnvironmentAwareManagedTask(instanceContext, newHandle, new TimeIntervalPollingController(newHandle, pollingInterval)));
      } catch (OperationFailureException ofe) {
         LOG.error(ofe.getMessage(), ofe);
         cancellationRemote.cancel();
      }

      return newHandle;
   }

   private void addTask(InstanceContext<? extends ReclaimableTask> runnableContext, TaskHandle newHandle, PollingController pollingController) throws OperationFailureException {
      ((InstanceContext<ReclaimableTask>) runnableContext).<TaskHandle>perform(ENLISTED_OPERATION, newHandle);
      taskTracker.enlistAction(new EnvironmentAwareManagedTask(runnableContext, newHandle, pollingController));
   }
}
