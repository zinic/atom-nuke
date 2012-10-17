package org.atomnuke.service.operation;

import org.atomnuke.plugin.operation.OperationFailureException;
import org.atomnuke.plugin.operation.SimpleOperation;
import org.atomnuke.service.ServiceLifeCycle;

/**
 *
 * @author zinic
 */
public class ServiceDestroyOperation implements SimpleOperation<ServiceLifeCycle> {

   private static final SimpleOperation<ServiceLifeCycle> INSTANCE = new ServiceDestroyOperation();

   public static <T extends ServiceLifeCycle> SimpleOperation<T> instance() {
      return (SimpleOperation<T>) INSTANCE;
   }

   @Override
   public void perform(ServiceLifeCycle instance) throws OperationFailureException {
      instance.destroy();
   }
}
