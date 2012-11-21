package org.atomnuke.category.index;

import com.sun.jersey.spi.container.servlet.ServletContainer;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.atomnuke.atom.model.Category;
import org.atomnuke.atom.model.Entry;
import org.atomnuke.atom.model.Feed;
import org.atomnuke.category.index.jaxrs.CategoryIndexResource;
import org.atomnuke.sink.AtomSink;
import org.atomnuke.sink.AtomSinkException;
import org.atomnuke.sink.AtomSinkResult;
import org.atomnuke.sink.SinkResult;
import org.atomnuke.task.context.AtomTaskContext;
import org.atomnuke.lifecycle.InitializationException;
import org.atomnuke.service.ServiceUnavailableException;
import org.atomnuke.service.jetty.server.ContextBuilder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class CategoryIndex implements AtomSink {

   private static final Logger LOG = LoggerFactory.getLogger(CategoryIndex.class);

   private final CategoryTracker categoryTracker;
   private ServletContextHandler servletContextHandler;

   public CategoryIndex() {
      categoryTracker = new CategoryTrackerImpl();
   }

   public void indexCategories(List<Category> categories) {
      for (Category indexCat : categories) {
         if (StringUtils.isNotBlank(indexCat.scheme()) || StringUtils.isNotBlank(indexCat.term())) {
            final String indexKey = indexCat.scheme() + "_" + indexCat.term();
            categoryTracker.setCategory(indexKey, indexCat);
         }
      }
   }

   @Override
   public SinkResult entry(Entry entry) throws AtomSinkException {
      indexCategories(entry.categories());

      return AtomSinkResult.ok();
   }

   @Override
   public SinkResult feedPage(Feed page) throws AtomSinkException {
      indexCategories(page.categories());

      for (Entry e : page.entries()) {
         indexCategories(e.categories());
      }

      return AtomSinkResult.ok();
   }

   @Override
   public void init(AtomTaskContext context) throws InitializationException {
      try {
         final ContextBuilder contextBuilder = context.services().firstAvailable(ContextBuilder.class);

         servletContextHandler = contextBuilder.newContext("/categoryIndex");
         initServletContext(servletContextHandler);
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

   private void initServletContext(ServletContextHandler context) {
      // Set up the subscription manager
      context.setAttribute(CategoryIndexResource.CAT_TRACKER_CTX_NAME, categoryTracker);

      // Register the JAX-RS servlet
      final ServletHolder servletInstance = new ServletHolder(ServletContainer.class);

      servletInstance.setInitParameter("com.sun.jersey.config.property.packages", "org.atomnuke.category.index.jaxrs");
      servletInstance.setInitParameter("com.sun.jersey.api.json.POJOMappingFeature", "true");

      context.addServlet(servletInstance, "/*");
   }
}
