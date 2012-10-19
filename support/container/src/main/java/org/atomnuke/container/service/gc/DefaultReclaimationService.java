package org.atomnuke.container.service.gc;

import org.atomnuke.container.service.annotation.NukeBootstrap;
import org.atomnuke.container.service.gc.ReclaimationHandler;
import org.atomnuke.container.service.gc.impl.NukeReclaimationHandler;
import org.atomnuke.service.Service;
import org.atomnuke.service.context.ServiceContext;
import org.atomnuke.util.lifecycle.InitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
@NukeBootstrap
public class DefaultReclaimationService implements Service {

   private static final String SERVICE_NAME = "org.atomnuke.container.service.gc.ReclaimationService";
   private static final Logger LOG = LoggerFactory.getLogger(DefaultReclaimationService.class);

   private final ReclaimationHandler reclaimationHandler;

   public DefaultReclaimationService() {
      reclaimationHandler = new NukeReclaimationHandler();
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
      LOG.info("DEBUG");
   }

   @Override
   public void destroy() {
      reclaimationHandler.destroy();
   }
}
