package org.atomnuke.service.operation;

import org.atomnuke.plugin.operation.ComplexOperation;
import org.atomnuke.plugin.operation.OperationFailureException;
import org.atomnuke.service.Service;
import org.atomnuke.service.resolution.ResolutionAction;

/**
 *
 * @author zinic
 */
public class ServiceResolveOperation implements ComplexOperation<Service, ServiceResolutionArgument> {

   private static final ComplexOperation<Service, ServiceResolutionArgument> INSTANCE = new ServiceResolveOperation();

   public static ComplexOperation<Service, ServiceResolutionArgument> instance() {
      return INSTANCE;
   }

   @Override
   public void perform(Service instance, ServiceResolutionArgument argument) throws OperationFailureException {
      final ResolutionAction action = instance.resolve(argument.serviceManager());
      argument.setResolutionAction(action);
   }
}
