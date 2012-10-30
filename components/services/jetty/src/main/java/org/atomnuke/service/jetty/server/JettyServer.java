package org.atomnuke.service.jetty.server;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.nio.SelectChannelConnector;

/**
 *
 * @author zinic
 */
public class JettyServer {

   private final ContextBuilder contextBuilder;
   private final Server server;

   public JettyServer() {
      final ContextHandlerCollection contextHandlerCollection = new ContextHandlerCollection();

      // Set up the server
      server = new Server();
      server.setHandler(contextHandlerCollection);

      // Bind the collection to our context builder
      contextBuilder = new ContextBuilderImpl(contextHandlerCollection);
   }

   public ContextBuilder getContextBuilder() {
      return contextBuilder;
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

   public void start(int portNumber) throws Exception {
      final Connector nioConnector = new SelectChannelConnector();
      nioConnector.setPort(portNumber);

      server.addConnector(nioConnector);
      server.start();

      while (server.isStarting()) {
         try {
            Thread.sleep(100);
         } catch (InterruptedException ie) {
            break;
         }
      }
   }

   public void stop() throws Exception {
      server.stop();
      server.join();
   }
}
