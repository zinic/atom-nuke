package org.atomnuke.servlet.jetty;

import org.atomnuke.listener.AtomListener;
import org.atomnuke.servlet.AtomSinkServlet;
import org.atomnuke.source.QueueSource;
import org.atomnuke.task.lifecycle.InitializationException;
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
      try {
         rootContext.addServlet(new ServletHolder(new AtomSinkServlet(queueSource)), "/*");
      } catch (InitializationException ie) {
         throw new RuntimeException("Failed to start. Reason: " + ie.getMessage());
      }
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
