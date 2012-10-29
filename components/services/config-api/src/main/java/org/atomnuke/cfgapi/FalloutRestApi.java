package org.atomnuke.cfgapi;

import org.atomnuke.cfgapi.jetty.JettyServer;
import org.atomnuke.container.service.annotation.NukeService;
import org.atomnuke.service.Service;
import org.atomnuke.service.ServiceManager;
import org.atomnuke.service.ServiceUnavailableException;
import org.atomnuke.service.context.ServiceContext;
import org.atomnuke.service.resolution.ResolutionAction;
import org.atomnuke.service.resolution.ResolutionActionImpl;
import org.atomnuke.service.resolution.ResolutionActionType;
import org.atomnuke.util.config.update.ConfigurationUpdateManager;
import org.atomnuke.util.lifecycle.InitializationException;
import org.atomnuke.util.service.ServiceHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
@NukeService
public class FalloutRestApi implements Service {

   private static final Logger LOG = LoggerFactory.getLogger(FalloutRestApi.class);

   private static final String SVC_NAME = "org.atomnuke.cfgapi.FalloutRestApi";
   private JettyServer jettyServer;

   @Override
   public String name() {
      return SVC_NAME;
   }

   @Override
   public boolean provides(Class serviceInterface) {
      return false;
   }

   @Override
   public Object instance() {
      return this;
   }

   @Override
   public ResolutionAction resolve(ServiceManager serviceManager) {
      return new ResolutionActionImpl(serviceManager.serviceRegistered(ConfigurationUpdateManager.class) ? ResolutionActionType.INIT : ResolutionActionType.DEFER);
   }

   @Override
   public void init(ServiceContext contextObject) throws InitializationException {
      try {
         jettyServer = new JettyServer(ServiceHandler.instance().firstAvailable(contextObject.manager(), ConfigurationUpdateManager.class), 8778);
      } catch (ServiceUnavailableException sue) {
         throw new InitializationException(sue);
      }

      try {
         jettyServer.start();

         while (!jettyServer.isStarted()) {
            Thread.sleep(100);
         }
      } catch (Exception ex) {
         throw new InitializationException(ex.getMessage(), ex.getCause());
      }
   }
   @Override
   public void destroy() {
      try {
         jettyServer.stop();

         while (!jettyServer.isStopped() && !jettyServer.isFailed()) {
            Thread.sleep(100);
         }
      } catch (Exception ex) {
         LOG.error("Failed to destroy HTTPSource. Reason: " + ex.getMessage(), ex);
      }
   }
}
