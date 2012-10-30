package org.atomnuke.control.service;

import com.sun.jersey.spi.container.servlet.ServletContainer;
import org.atomnuke.container.service.annotation.NukeService;
import org.atomnuke.service.Service;
import org.atomnuke.service.ServiceManager;
import org.atomnuke.service.ServiceUnavailableException;
import org.atomnuke.service.context.ServiceContext;
import org.atomnuke.service.jetty.server.ContextBuilder;
import org.atomnuke.service.resolution.ResolutionAction;
import org.atomnuke.service.resolution.ResolutionActionImpl;
import org.atomnuke.service.resolution.ResolutionActionType;
import org.atomnuke.util.config.update.ConfigurationUpdateManager;
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
@NukeService
public class FalloutControlApiService implements Service {

   private static final Logger LOG = LoggerFactory.getLogger(FalloutControlApiService.class);
   private static final String SVC_NAME = "org.atomnuke.control.service.FalloutControlApiService";

   private ServletContextHandler servletContextHandler;

   @Override
   public String name() {
      return SVC_NAME;
   }

   @Override
   public boolean provides(Class serviceInterface) {
      return false;
   }

   @Override
   public Object instance() {
      return this;
   }

   @Override
   public ResolutionAction resolve(ServiceManager serviceManager) {
      final boolean hasConfigurationService = serviceManager.serviceRegistered(ConfigurationUpdateManager.class);
      final boolean hasJettyService = serviceManager.serviceRegistered(ContextBuilder.class);

      return hasConfigurationService && hasJettyService ? new ResolutionActionImpl(ResolutionActionType.INIT) : new ResolutionActionImpl(ResolutionActionType.DEFER);
   }

   @Override
   public void init(ServiceContext serviceCtx) throws InitializationException {
      try {
         final ContextBuilder contextBuilder = ServiceHandler.instance().firstAvailable(serviceCtx.manager(), ContextBuilder.class);
         servletContextHandler = contextBuilder.newContext("/control/api");
      } catch (ServiceUnavailableException sue) {
         throw new InitializationException(sue);
      }

      // Register the JAX-RS servlet
      final ServletHolder servletInstance = new ServletHolder(ServletContainer.class);

      servletInstance.setInitParameter("com.sun.jersey.config.property.packages", "org.atomnuke.control.api");
      servletInstance.setInitParameter("com.sun.jersey.api.json.POJOMappingFeature", "true");

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
