package org.atomnuke.service.jetty;

import org.atomnuke.container.service.annotation.NukeService;
import org.atomnuke.service.Service;
import org.atomnuke.service.ServiceManager;
import org.atomnuke.service.context.ServiceContext;
import org.atomnuke.service.jetty.server.ContextBuilder;
import org.atomnuke.service.jetty.server.JettyServer;
import org.atomnuke.service.resolution.ResolutionAction;
import org.atomnuke.service.resolution.ResolutionActionImpl;
import org.atomnuke.service.resolution.ResolutionActionType;
import org.atomnuke.util.lifecycle.InitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
@NukeService
public class JettyService implements Service {

   private static final Logger LOG = LoggerFactory.getLogger(JettyService.class);
   private static final String SVC_NAME = "org.atomnuke.service.jetty.JettyService";
   
   private final JettyServer jettyServer;

   public JettyService() {
      jettyServer = new JettyServer();
   }

   @Override
   public String name() {
      return SVC_NAME;
   }

   @Override
   public boolean provides(Class serviceInterface) {
      return serviceInterface.isAssignableFrom(ContextBuilder.class);
   }

   @Override
   public Object instance() {
      return jettyServer.getContextBuilder();
   }

   @Override
   public ResolutionAction resolve(ServiceManager serviceManager) {
      return new ResolutionActionImpl(ResolutionActionType.INIT);
   }

   @Override
   public void init(ServiceContext contextObject) throws InitializationException {
      try {
         jettyServer.start(8080);
      } catch (Exception ex) {
         throw new InitializationException(ex);
      }
   }

   @Override
   public void destroy() {
      try {
         jettyServer.stop();
      } catch (Exception ex) {
         LOG.error(ex.getMessage(), ex);
      }
   }
}
