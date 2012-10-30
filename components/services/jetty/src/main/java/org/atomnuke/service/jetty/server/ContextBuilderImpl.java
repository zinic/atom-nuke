package org.atomnuke.service.jetty.server;

import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class ContextBuilderImpl implements ContextBuilder {

   private static final Logger LOG = LoggerFactory.getLogger(ContextBuilderImpl.class);

   private final ContextHandlerCollection handlerCollection;

   public ContextBuilderImpl(ContextHandlerCollection handlerCollection) {
      this.handlerCollection = handlerCollection;
   }

   @Override
   public synchronized ServletContextHandler newContext(String contextPath) {
      final ServletContextHandler newHandler = new ServletContextHandler(handlerCollection, contextPath);
      try {
         newHandler.start();

         handlerCollection.mapContexts();

         return newHandler;
      } catch (Exception ex) {
         LOG.error(ex.getMessage(), ex);
      }

      return null;
   }
}
