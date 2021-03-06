package org.atomnuke.task.atom.impl;

import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.plugin.operation.OperationFailureException;
import org.atomnuke.plugin.operation.SimpleOperation;
import org.atomnuke.task.ManagedTask;
import org.atomnuke.task.TaskHandle;
import org.atomnuke.task.polling.PollingController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class EnvironmentAwareManagedTask<T extends TaskHandle> implements ManagedTask {

   private static final Logger LOG = LoggerFactory.getLogger(EnvironmentAwareManagedTask.class);
   
   private static final SimpleOperation<Runnable> RUN_OPERATION = new SimpleOperation<Runnable>() {
      @Override
      public void perform(Runnable instance) throws OperationFailureException {
         instance.run();
      }
   };
   
   private final InstanceContext<Runnable> runnableCtx;
   private final PollingController pollingController;
   private final T taskHandle;

   public EnvironmentAwareManagedTask(InstanceContext<Runnable> runnableCtx, T taskHandle, PollingController pollingController) {
      this.pollingController = pollingController;
      this.runnableCtx = runnableCtx;
      this.taskHandle = taskHandle;
   }

   @Override
   public PollingController pollingController() {
      return pollingController;
   }

   @Override
   public final T handle() {
      return taskHandle;
   }

   @Override
   public final void run() {
      try {
         runnableCtx.perform(RUN_OPERATION);
      } catch (OperationFailureException ofe) {
         LOG.error(ofe.getMessage(), ofe);
      }
   }
}
