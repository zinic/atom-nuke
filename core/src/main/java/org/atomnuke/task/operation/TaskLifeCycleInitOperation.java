package org.atomnuke.task.operation;

import org.atomnuke.plugin.operation.ComplexOperation;
import org.atomnuke.plugin.operation.OperationFailureException;
import org.atomnuke.task.context.AtomTaskContext;
import org.atomnuke.task.lifecycle.InitializationException;
import org.atomnuke.task.lifecycle.TaskLifeCycle;

/**
 *
 * @author zinic
 */
public class TaskLifeCycleInitOperation implements ComplexOperation<TaskLifeCycle, AtomTaskContext> {

   private static final ComplexOperation<TaskLifeCycle, AtomTaskContext> INSTANCE = new TaskLifeCycleInitOperation();

   public static <T extends TaskLifeCycle> ComplexOperation<T, AtomTaskContext> instance() {
      return (ComplexOperation<T, AtomTaskContext>) INSTANCE;
   }

   @Override
   public void perform(TaskLifeCycle instance, AtomTaskContext argument) throws OperationFailureException {
      try {
         instance.init(argument);
      } catch (InitializationException ie) {
         throw new OperationFailureException(ie);
      }
   }
}
