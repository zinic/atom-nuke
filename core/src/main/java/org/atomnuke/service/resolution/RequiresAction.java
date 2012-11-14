package org.atomnuke.service.resolution;

import org.atomnuke.lifecycle.resolution.ResolutionAction;
import org.atomnuke.lifecycle.resolution.ResolutionActionType;
import org.atomnuke.service.ServiceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class RequiresAction implements ResolutionAction {

   private static final Logger LOG = LoggerFactory.getLogger(RequiresAction.class);

   private final ResolutionActionType actionType;

   public RequiresAction(ServiceManager serviceManager, Class... requiredInterfaces) {
      actionType = resolveAction(serviceManager, requiredInterfaces);
   }

   private static ResolutionActionType resolveAction(ServiceManager serviceManager, Class... requiredInterfaces) {
      for (Class requiredInterface : requiredInterfaces) {
         if (!serviceManager.serviceRegistered(requiredInterface)) {
            LOG.info("Unable to locate service for: " + requiredInterface.getName() + " - deferring.");
            
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
