package org.atomnuke.service.operation;

import org.atomnuke.service.resolution.ResolutionActionType;
import org.atomnuke.service.ServiceManager;
import org.atomnuke.service.resolution.ResolutionAction;
import org.atomnuke.service.resolution.ResolutionActionImpl;

/**
 *
 * @author zinic
 */
public class ServiceResolutionArgument {

   private final ServiceManager serviceManager;
   private ResolutionAction resolutionAction;

   public ServiceResolutionArgument(ServiceManager serviceManager) {
      this.serviceManager = serviceManager;

      resolutionAction = new ResolutionActionImpl(ResolutionActionType.FAIL);
   }

   public ServiceManager serviceManager() {
      return serviceManager;
   }

   public ResolutionAction resolutionAction() {
      return resolutionAction;
   }

   public void setResolutionAction(ResolutionAction resolutionAction) {
      this.resolutionAction = resolutionAction;
   }
}
