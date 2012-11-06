package org.atomnuke.service.resolution;

import org.atomnuke.service.ServiceManager;

/**
 *
 * @author zinic
 */
public class RequiresAction implements ResolutionAction {

   private final ResolutionActionType actionType;

   public RequiresAction(ServiceManager serviceManager, Class... requiredInterfaces) {
      actionType = resolveAction(serviceManager, requiredInterfaces);
   }

   private static ResolutionActionType resolveAction(ServiceManager serviceManager, Class... requiredInterfaces) {
      for (Class requiredInterface : requiredInterfaces) {
         if (!serviceManager.serviceRegistered(requiredInterface)) {
            return ResolutionActionType.DEFER;
         }
      }

      return ResolutionActionType.INIT;
   }

   @Override
   public ResolutionActionType type() {
      return actionType;
   }
}
