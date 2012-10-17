package org.atomnuke.task.operation;

import org.atomnuke.plugin.operation.OperationFailureException;
import org.atomnuke.plugin.operation.SimpleOperation;
import org.atomnuke.task.lifecycle.TaskLifeCycle;

/**
 *
 * @author zinic
 */
public class TaskLifeCycleDestroyOperation implements SimpleOperation<TaskLifeCycle> {

   private static final SimpleOperation<TaskLifeCycle> INSTANCE = new TaskLifeCycleDestroyOperation();

   public static <T extends TaskLifeCycle> SimpleOperation<T> instance() {
      return (SimpleOperation<T>) INSTANCE;
   }

   @Override
   public void perform(TaskLifeCycle instance) throws OperationFailureException {
      instance.destroy();
   }
}
