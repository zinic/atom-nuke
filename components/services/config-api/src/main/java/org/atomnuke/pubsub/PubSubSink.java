package org.atomnuke.pubsub;

import org.atomnuke.pubsub.sub.TemporarySubscriptionManager;
import org.atomnuke.pubsub.sub.SubscriptionManager;
import com.sun.jersey.spi.container.servlet.ServletContainer;
import org.atomnuke.service.ServiceUnavailableException;
import org.atomnuke.service.jetty.server.ContextBuilder;
import org.atomnuke.sink.eps.FanoutSink;
import org.atomnuke.task.context.AtomTaskContext;
import org.atomnuke.util.config.update.ConfigurationContext;
import org.atomnuke.util.lifecycle.InitializationException;
import org.atomnuke.util.service.ServiceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class PubSubSink extends FanoutSink {

   private static final Logger LOG = LoggerFactory.getLogger(PubSubSink.class);

   private ServletContextHandler servletContextHandler;
   private ConfigurationContext cfgContext;

   @Override
   public void init(AtomTaskContext context) throws InitializationException {
      // Make sure we let our parent know that were initializing
      super.init(context);

      try {
         final ContextBuilder contextBuilder = ServiceHandler.instance().firstAvailable(context.services(), ContextBuilder.class);
         final SubscriptionManager subManager = new TemporarySubscriptionManager(this);

         initServletContext(contextBuilder.newContext("/control/api"), subManager);
      } catch (ServiceUnavailableException sue) {
         throw new InitializationException(sue);
      }
   }

   @Override
   public void destroy() {
      cfgContext.cancellationRemote().cancel();

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
      servletContextHandler = context;

      // Set up the subscription manager
      context.setAttribute(SubscriptionManager.SERVLET_CTX_NAME, subManager);

      // Register the JAX-RS servlet
      final ServletHolder servletInstance = new ServletHolder(ServletContainer.class);

      servletInstance.setInitParameter("com.sun.jersey.config.property.packages", "org.atomnuke.control.api");
      servletInstance.setInitParameter("com.sun.jersey.api.json.POJOMappingFeature", "true");

      context.addServlet(servletInstance, "/*");
   }
}
