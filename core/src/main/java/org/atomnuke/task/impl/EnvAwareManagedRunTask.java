package org.atomnuke.task.impl;

import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.plugin.operation.OperationFailureException;
import org.atomnuke.plugin.operation.SimpleOperation;
import org.atomnuke.task.TaskHandle;

/**
 *
 * @author zinic
 */
public class EnvAwareManagedRunTask extends AbstractManagedTask {

   private static final SimpleOperation<Runnable> RUN_OPERATION = new SimpleOperation<Runnable>() {
      @Override
      public void perform(Runnable instance) throws OperationFailureException {
         instance.run();
      }
   };

   private final InstanceContext<Runnable> runnable;

   public EnvAwareManagedRunTask(InstanceContext<? extends Runnable> runnable, TaskHandle taskHandle) {
      super(taskHandle);

      this.runnable = (InstanceContext<Runnable>) runnable;
   }

   @Override
   public void run() {
      runnable.perform(RUN_OPERATION);
   }
}
