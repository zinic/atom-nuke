package org.atomnuke.pubsub.sink;

import org.atomnuke.pubsub.sub.TemporarySubscriptionManager;
import org.atomnuke.pubsub.sub.SubscriptionManager;
import com.sun.jersey.spi.container.servlet.ServletContainer;
import org.apache.http.client.HttpClient;
import org.atomnuke.service.ServiceUnavailableException;
import org.atomnuke.service.jetty.server.ContextBuilder;
import org.atomnuke.sink.eps.EventletChainSink;
import org.atomnuke.task.context.AtomTaskContext;
import org.atomnuke.lifecycle.InitializationException;
import org.atomnuke.util.service.ServiceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class PubSubSink extends EventletChainSink {

   private static final Logger LOG = LoggerFactory.getLogger(PubSubSink.class);

   private ServletContextHandler servletContextHandler;

   @Override
   public void init(AtomTaskContext context) throws InitializationException {
      // Make sure we let our parent know that were initializing
      super.init(context);

      try {
         final ContextBuilder contextBuilder = ServiceHandler.instance().firstAvailable(context.services(), ContextBuilder.class);
         final SubscriptionManager subManager = new TemporarySubscriptionManager(ServiceHandler.instance().firstAvailable(context.services(), HttpClient.class), this);

         servletContextHandler = contextBuilder.newContext("/pubsub");
         initServletContext(servletContextHandler, subManager);
      } catch (ServiceUnavailableException sue) {
         throw new InitializationException(sue);
      }
   }

   @Override
   public void destroy() {
      if (servletContextHandler != null && !(servletContextHandler.isStopping() || servletContextHandler.isStopped())) {
         try {
            servletContextHandler.stop();
            servletContextHandler.destroy();
         } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
         }
      }
   }

   private void initServletContext(ServletContextHandler context, SubscriptionManager subManager) {
      // Set up the subscription manager
      context.setAttribute(SubscriptionManager.SERVLET_CTX_NAME, subManager);

      // Register the JAX-RS servlet
      final ServletHolder servletInstance = new ServletHolder(ServletContainer.class);

      servletInstance.setInitParameter("com.sun.jersey.config.property.packages", "org.atomnuke.pubsub.api");
      servletInstance.setInitParameter("com.sun.jersey.api.json.POJOMappingFeature", "true");

      context.addServlet(servletInstance, "/*");
   }
}
