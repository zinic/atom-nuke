package org.atomnuke.service.operation;

import org.atomnuke.plugin.operation.ComplexOperation;
import org.atomnuke.plugin.operation.OperationFailureException;
import org.atomnuke.service.ServiceInitializationException;
import org.atomnuke.service.lifecycle.ServiceLifeCycle;
import org.atomnuke.service.context.ServiceContext;

/**
 *
 * @author zinic
 */
public class ServiceInitOperation implements ComplexOperation<ServiceLifeCycle, ServiceContext> {

   private static final ComplexOperation<ServiceLifeCycle, ServiceContext> INSTANCE = new ServiceInitOperation();

   public static <T extends ServiceLifeCycle> ComplexOperation<T, ServiceContext> instance() {
      return (ComplexOperation<T, ServiceContext>) INSTANCE;
   }

   @Override
   public void perform(ServiceLifeCycle instance, ServiceContext argument) throws OperationFailureException {
      try {
         instance.init(argument);
      } catch (ServiceInitializationException ie) {
         throw new OperationFailureException(ie);
      }
   }
}
