package org.atomnuke.collectd.servlet.jetty;

import org.atomnuke.collectd.servlet.CollectdSinkServlet;
import org.atomnuke.collectd.source.QueueSource;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
// <3 you Jetty, I really do.
public class JettyServer {

   private static final Logger LOG = LoggerFactory.getLogger(JettyServer.class);
   private final ServletContextHandler rootContext;
   private final Server server;

   public JettyServer(int portNumber, QueueSource queueSource) {
      server = new Server(portNumber);
      rootContext = new ServletContextHandler(server, "/");

      init(queueSource);
   }

   private void init(QueueSource queueSource) {
//      try {
         rootContext.addServlet(new ServletHolder(new CollectdSinkServlet(queueSource)), "/*");
//      } catch (InitializationException ie) {
//         throw new RuntimeException("Failed to start. Reason: " + ie.getMessage());
//      }
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
      try {
         server.start();
      } catch (Exception ex) {
         LOG.error("error occurred in start", ex);
         throw ex;
      }
   }

   public void stop() throws Exception {
      try {
         server.stop();
      } catch (Exception ex) {
         LOG.error("error occurred in stop", ex);
         throw ex;
      }
   }
}
