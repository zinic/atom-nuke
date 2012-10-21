package org.atomnuke.service.operation;

import org.atomnuke.service.ResolutionAction;
import org.atomnuke.service.ServiceManager;

/**
 *
 * @author zinic
 */
public class ServiceResolutionArgument {

   private final ServiceManager serviceManager;
   private ResolutionAction resolutionAction;

   public ServiceResolutionArgument(ServiceManager serviceManager) {
      this.serviceManager = serviceManager;
      
      resolutionAction = ResolutionAction.FAIL;
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
