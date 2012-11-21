package org.atomnuke.fallout.service.gc;

import org.atomnuke.container.service.annotation.NukeBootstrap;
import org.atomnuke.service.gc.ReclamationHandler;
import org.atomnuke.service.gc.impl.NukeReclamationHandler;
import org.atomnuke.service.runtime.AbstractRuntimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
@NukeBootstrap
public class FalloutReclamationService extends AbstractRuntimeService {

   private static final Logger LOG = LoggerFactory.getLogger(FalloutReclamationService.class);

   private final ReclamationHandler reclamationHandler;

   public FalloutReclamationService() {
      super(ReclamationHandler.class);

      reclamationHandler = new NukeReclamationHandler();
   }

   @Override
   public Object instance() {
      return reclamationHandler;
   }

   @Override
   public void destroy() {
      LOG.info("Reclaiming all registered reclamation handles.");
      reclamationHandler.destroy();
   }
}
