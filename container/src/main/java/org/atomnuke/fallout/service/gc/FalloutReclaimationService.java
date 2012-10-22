package org.atomnuke.fallout.service.gc;

import org.atomnuke.container.service.annotation.NukeBootstrap;
import org.atomnuke.service.ResolutionAction;
import org.atomnuke.service.gc.ReclaimationHandler;
import org.atomnuke.service.gc.impl.NukeReclaimationHandler;
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
public class FalloutReclaimationService implements Service {

   private static final String SERVICE_NAME = "org.atomnuke.container.service.gc.ReclaimationService";
   private static final Logger LOG = LoggerFactory.getLogger(FalloutReclaimationService.class);

   private final ReclaimationHandler reclaimationHandler;

   public FalloutReclaimationService() {
      reclaimationHandler = new NukeReclaimationHandler();
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
      return serviceInterface.isAssignableFrom(ReclaimationHandler.class);
   }

   @Override
   public Object instance() {
      return reclaimationHandler;
   }

   @Override
   public void init(ServiceContext contextObject) throws InitializationException {
      LOG.info("Fallout reclaimation serivce initialized.");
   }

   @Override
   public void destroy() {
      LOG.info("Reclaiming all registered reclaimation handles.");
      reclaimationHandler.destroy();

      LOG.info("Fallout reclaimation serivce destroyed.");
   }
}
