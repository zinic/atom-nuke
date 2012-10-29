package org.atomnuke.cfgapi.jetty;

import com.sun.jersey.spi.container.servlet.ServletContainer;
import org.atomnuke.util.config.update.ConfigurationUpdateManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 *
 * @author zinic
 */
public class JettyServer {

   private final ConfigurationUpdateManager cfgUpdateManager;
   private final ServletContextHandler rootContext;
   private final Server server;

   public JettyServer(ConfigurationUpdateManager cfgUpdateManager, int portNumber) {
      this.cfgUpdateManager = cfgUpdateManager;

      server = new Server(portNumber);
      rootContext = new ServletContextHandler(server, "/");

      init();
   }

   private void init() {
//      cfgUpdateManager.register("fallout-server-cfg", null);

      // Register the JAX-RS servlet
      final ServletHolder servletInstance = new ServletHolder(ServletContainer.class);

      servletInstance.setInitParameter("com.sun.jersey.config.property.packages", "org.atomnuke.cfgapi.jaxrs");
      servletInstance.setInitParameter("com.sun.jersey.api.json.POJOMappingFeature", "true");

      rootContext.addServlet(servletInstance, "/*");
      rootContext.setClassLoader(Thread.currentThread().getContextClassLoader());
   }

   public boolean isRunning() {
      return server.isRunning();
   }

   public boolean isStarted() {
      return server.isStarted();
   }

   public boolean isStarting() {
      return server.isStarting();
   }

   public boolean isStopping() {
      return server.isStopping();
   }

   public boolean isStopped() {
      return server.isStopped();
   }

   public boolean isFailed() {
      return server.isFailed();
   }

   public void start() throws Exception {
      server.start();
   }

   public void stop() throws Exception {
      server.stop();
   }
}
