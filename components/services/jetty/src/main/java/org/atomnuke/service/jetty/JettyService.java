package org.atomnuke.service.jetty;

import org.atomnuke.container.service.annotation.NukeService;
import org.atomnuke.lifecycle.InitializationException;
import org.atomnuke.service.ServiceContext;
import org.atomnuke.service.ServiceManager;
import org.atomnuke.service.jetty.server.ContextBuilder;
import org.atomnuke.service.jetty.server.JettyServer;
import org.atomnuke.lifecycle.resolution.ResolutionAction;
import org.atomnuke.lifecycle.resolution.ResolutionActionImpl;
import org.atomnuke.lifecycle.resolution.ResolutionActionType;
import org.atomnuke.service.runtime.AbstractRuntimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
@NukeService
public class JettyService extends AbstractRuntimeService {

   private static final Logger LOG = LoggerFactory.getLogger(JettyService.class);
   private static final String SVC_NAME = "org.atomnuke.service.jetty.JettyService";

   private final JettyServer jettyServer;

   public JettyService() {
      super(ContextBuilder.class);

      jettyServer = new JettyServer();
   }

   @Override
   public String name() {
      return SVC_NAME;
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
   public void init(ServiceContext context) throws InitializationException {
      try {
         jettyServer.start(Integer.parseInt(context.environment().fromEnv("JETTY_PORT", "8080")));
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
