package org.atomnuke.http;

import org.atomnuke.service.ServiceUnavailableException;
import org.atomnuke.service.jetty.server.ContextBuilder;
import org.atomnuke.servlet.AtomSinkServlet;
import org.atomnuke.source.AtomSource;
import org.atomnuke.source.AtomSourceException;
import org.atomnuke.source.result.AtomSourceResult;
import org.atomnuke.source.QueueSource;
import org.atomnuke.source.QueueSourceImpl;
import org.atomnuke.task.context.AtomTaskContext;
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
public class HttpSource implements AtomSource {

   private static final Logger LOG = LoggerFactory.getLogger(HttpSource.class);

   private final QueueSource queueSource;
   private ServletContextHandler servletContextHandler;

   public HttpSource() {
      queueSource = new QueueSourceImpl();
   }

   @Override
   public AtomSourceResult poll() throws AtomSourceException {
      return queueSource.poll();
   }

   @Override
   public void init(AtomTaskContext tc) throws InitializationException {
      try {
         final ContextBuilder contextBuilder = ServiceHandler.instance().firstAvailable(tc.services(), ContextBuilder.class);
         servletContextHandler = contextBuilder.newContext("/publish");
      } catch (ServiceUnavailableException sue) {
         LOG.error("The CollecD source requires a service that provides a ContextBuilder implementation for regsitering servlets.");
         throw new InitializationException(sue);
      }

      // Register the JAX-RS servlet
      final ServletHolder servletInstance = new ServletHolder(new AtomSinkServlet(queueSource));
      servletContextHandler.addServlet(servletInstance, "/*");
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
}
