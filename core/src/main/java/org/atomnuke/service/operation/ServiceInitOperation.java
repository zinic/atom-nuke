package org.atomnuke.service.operation;

import org.atomnuke.plugin.operation.ComplexOperation;
import org.atomnuke.plugin.operation.OperationFailureException;
import org.atomnuke.service.ServiceContext;
import org.atomnuke.lifecycle.InitializationException;
import org.atomnuke.lifecycle.ResourceLifeCycle;

/**
 *
 * @author zinic
 */
public class ServiceInitOperation implements ComplexOperation<ResourceLifeCycle<ServiceContext>, ServiceContext> {

   private static final ComplexOperation<ResourceLifeCycle<ServiceContext>, ServiceContext> INSTANCE = new ServiceInitOperation();

   public static <T extends ResourceLifeCycle<ServiceContext>> ComplexOperation<T, ServiceContext> instance() {
      return (ComplexOperation<T, ServiceContext>) INSTANCE;
   }

   @Override
   public void perform(ResourceLifeCycle<ServiceContext> instance, ServiceContext argument) throws OperationFailureException {
      try {
         instance.init(argument);
      } catch (InitializationException ie) {
         throw new OperationFailureException(ie);
      }
   }
}
