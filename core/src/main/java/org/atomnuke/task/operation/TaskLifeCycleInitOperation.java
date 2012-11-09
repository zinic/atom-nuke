package org.atomnuke.task.operation;

import org.atomnuke.plugin.operation.ComplexOperation;
import org.atomnuke.plugin.operation.OperationFailureException;
import org.atomnuke.task.context.AtomTaskContext;
import org.atomnuke.lifecycle.InitializationException;
import org.atomnuke.lifecycle.ResourceLifeCycle;

/**
 *
 * @author zinic
 */
public class TaskLifeCycleInitOperation implements ComplexOperation<ResourceLifeCycle, AtomTaskContext> {

   private static final ComplexOperation<ResourceLifeCycle, AtomTaskContext> INSTANCE = new TaskLifeCycleInitOperation();

   public static <T extends ResourceLifeCycle> ComplexOperation<T, AtomTaskContext> instance() {
      return (ComplexOperation<T, AtomTaskContext>) INSTANCE;
   }

   @Override
   public void perform(ResourceLifeCycle instance, AtomTaskContext argument) throws OperationFailureException {
      try {
         instance.init(argument);
      } catch (InitializationException ie) {
         throw new OperationFailureException(ie);
      }
   }
}
