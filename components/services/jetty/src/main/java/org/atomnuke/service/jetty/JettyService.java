package org.atomnuke.service.jetty;

import org.atomnuke.container.service.annotation.NukeService;
import org.atomnuke.lifecycle.InitializationException;
import org.atomnuke.service.ServiceContext;
import org.atomnuke.service.jetty.server.ContextBuilder;
import org.atomnuke.service.jetty.server.JettyServer;
import org.atomnuke.service.jetty.version.VersionServlet;
import org.atomnuke.service.runtime.AbstractRuntimeService;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
@NukeService
public class JettyService extends AbstractRuntimeService {

   private static final Logger LOG = LoggerFactory.getLogger(JettyService.class);

   private final JettyServer jettyServer;

   public JettyService() {
      super(ContextBuilder.class);

      jettyServer = new JettyServer();
   }

   @Override
   public Object instance() {
      return jettyServer.getContextBuilder();
   }

   @Override
   public void init(ServiceContext context) throws InitializationException {
      try {
         jettyServer.start(Integer.parseInt(context.environment().fromEnv("JETTY_PORT", "8080")));
         
         // Version servlet
         final ServletHolder servletHolder = new ServletHolder(new VersionServlet());
         jettyServer.getContextBuilder().newContext("/_version").addServlet(servletHolder, "/*");
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
