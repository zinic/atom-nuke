package org.atomnuke.service;

import java.lang.annotation.Annotation;
import org.apache.commons.lang3.StringUtils;
import org.atomnuke.container.service.annotation.Requires;
import org.atomnuke.lifecycle.resolution.ResolutionAction;
import org.atomnuke.lifecycle.resolution.ResolutionActionImpl;
import org.atomnuke.lifecycle.resolution.ResolutionActionType;
import org.atomnuke.plugin.InstanceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class ResolutionHandlerImpl implements ResolutionHandler {

   private static final Logger LOG = LoggerFactory.getLogger(ResolutionHandlerImpl.class);
   private final ServiceManager serviceManager;

   public ResolutionHandlerImpl(ServiceManager serviceManager) {
      this.serviceManager = serviceManager;
   }

   @Override
   public ResolutionAction resolve(InstanceContext<? extends Service> serviceInstance) {
      final Class serviceInstanceClass = serviceInstance.instanceClass();
      final Annotation foundAnnotation = serviceInstanceClass.getAnnotation(Requires.class);

      ResolutionActionType resolutionAction = ResolutionActionType.INIT;

      if (foundAnnotation != null) {
         final Requires requires = (Requires) foundAnnotation;

         if (requires.value().length > 0) {
            for (Class requiredInterface : requires.value()) {
               if (!serviceManager.serviceRegistered(requiredInterface)) {
                  LOG.info("Unable to locate service for: " + requiredInterface.getName() + " - deferring.");
                  resolutionAction = ResolutionActionType.DEFER;

                  break;
               }
            }
         } else if (StringUtils.isNotBlank(requires.lookup())) {
            // TODO: Handle reading a services requirement descriptor
            LOG.info("Service resolution through a descriptor is not implemented yet. Bug John.");
            resolutionAction = ResolutionActionType.FAIL;
         }
      }

      return new ResolutionActionImpl(resolutionAction);
   }
}
