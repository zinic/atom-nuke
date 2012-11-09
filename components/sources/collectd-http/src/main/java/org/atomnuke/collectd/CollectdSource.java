package org.atomnuke.collectd;

import org.atomnuke.collectd.servlet.CollectdSinkServlet;
import org.atomnuke.source.AtomSource;
import org.atomnuke.source.AtomSourceException;
import org.atomnuke.source.result.AtomSourceResult;
import org.atomnuke.fallout.source.queue.QueueSource;
import org.atomnuke.fallout.source.queue.EntryQueueImpl;
import org.atomnuke.service.ServiceUnavailableException;
import org.atomnuke.service.jetty.server.ContextBuilder;
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
public class CollectdSource implements AtomSource {

   private static final Logger LOG = LoggerFactory.getLogger(CollectdSource.class);
   private static final String DEBUG_PARAM = "debug";

   private final QueueSource queueSource;
   private ServletContextHandler servletContextHandler;

   public CollectdSource() {
      queueSource = new EntryQueueImpl();
   }

   @Override
   public AtomSourceResult poll() throws AtomSourceException {
      return queueSource.poll();
   }

   @Override
   public void init(AtomTaskContext tc) throws InitializationException {
      try {
         final ContextBuilder contextBuilder = ServiceHandler.instance().firstAvailable(tc.services(), ContextBuilder.class);
         servletContextHandler = contextBuilder.newContext("/collectd");
      } catch (ServiceUnavailableException sue) {
         LOG.error("The CollecD source requires a service that provides a ContextBuilder implementation for regsitering servlets.");
         throw new InitializationException(sue);
      }

      // Register the JAX-RS servlet
      boolean debug = tc.parameters().containsKey(DEBUG_PARAM) && tc.parameters().get(DEBUG_PARAM).equalsIgnoreCase(Boolean.TRUE.toString());

      final ServletHolder servletInstance = new ServletHolder(new CollectdSinkServlet(queueSource, debug));
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
