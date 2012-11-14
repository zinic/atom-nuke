package org.atomnuke.fallout.service.gc;

import org.atomnuke.container.service.annotation.NukeBootstrap;
import org.atomnuke.service.gc.ReclamationHandler;
import org.atomnuke.service.gc.impl.NukeReclamationHandler;
import org.atomnuke.service.ServiceManager;
import org.atomnuke.service.ServiceContext;
import org.atomnuke.lifecycle.resolution.ResolutionAction;
import org.atomnuke.lifecycle.resolution.ResolutionActionType;
import org.atomnuke.lifecycle.resolution.ResolutionActionImpl;
import org.atomnuke.lifecycle.InitializationException;
import org.atomnuke.service.runtime.AbstractRuntimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
@NukeBootstrap
public class FalloutReclamationService extends AbstractRuntimeService {

   private static final String SERVICE_NAME = "org.atomnuke.fallout.service.gc.FalloutReclamationService";
   private static final Logger LOG = LoggerFactory.getLogger(FalloutReclamationService.class);

   private final ReclamationHandler reclamationHandler;

   public FalloutReclamationService() {
      super(ReclamationHandler.class);

      reclamationHandler = new NukeReclamationHandler();
   }

   @Override
   public ResolutionAction resolve(ServiceManager serviceManager) {
      return new ResolutionActionImpl(ResolutionActionType.INIT);
   }

   @Override
   public String name() {
      return SERVICE_NAME;
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
