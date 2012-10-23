package org.atomnuke.fallout.service.gc;

import org.atomnuke.container.service.annotation.NukeBootstrap;
import org.atomnuke.service.ResolutionAction;
import org.atomnuke.service.gc.ReclamationHandler;
import org.atomnuke.service.gc.impl.NukeReclamationHandler;
import org.atomnuke.service.Service;
import org.atomnuke.service.ServiceManager;
import org.atomnuke.service.context.ServiceContext;
import org.atomnuke.util.lifecycle.InitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
@NukeBootstrap
public class FalloutReclamationService implements Service {

   private static final String SERVICE_NAME = "org.atomnuke.fallout.service.gc.FalloutReclamationService";
   private static final Logger LOG = LoggerFactory.getLogger(FalloutReclamationService.class);

   private final ReclamationHandler reclamationHandler;

   public FalloutReclamationService() {
      reclamationHandler = new NukeReclamationHandler();
   }

   @Override
   public ResolutionAction resolve(ServiceManager serviceManager) {
      return ResolutionAction.INIT;
   }

   @Override
   public String name() {
      return SERVICE_NAME;
   }

   @Override
   public boolean provides(Class serviceInterface) {
      return serviceInterface.isAssignableFrom(ReclamationHandler.class);
   }

   @Override
   public Object instance() {
      return reclamationHandler;
   }

   @Override
   public void init(ServiceContext contextObject) throws InitializationException {
      LOG.info("Fallout reclamation serivce initialized.");
   }

   @Override
   public void destroy() {
      LOG.info("Reclaiming all registered reclamation handles.");
      reclamationHandler.destroy();

      LOG.info("Fallout reclamation serivce destroyed.");
   }
}
