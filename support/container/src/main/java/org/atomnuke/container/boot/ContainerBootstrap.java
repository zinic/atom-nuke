package org.atomnuke.container.boot;

import java.util.Collections;
import org.atomnuke.container.service.annotation.NukeBootstrap;
import org.atomnuke.plugin.InstanceContextImpl;
import org.atomnuke.plugin.local.LocalInstanceEnvironment;
import org.atomnuke.service.Service;
import org.atomnuke.service.ServiceManager;
import org.atomnuke.service.context.ServiceContext;
import org.atomnuke.service.context.ServiceContextImpl;
import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class ContainerBootstrap implements Bootstrap {

   private static final Logger LOG = LoggerFactory.getLogger(ContainerBootstrap.class);

   private final ServiceManager serviceManager;

   public ContainerBootstrap(ServiceManager serviceManager) {
      this.serviceManager = serviceManager;
   }

   @Override
   public void bootstrap() {
      final Reflections bootstrapScanner = new Reflections(new ConfigurationBuilder()
              .setScanners(new TypeAnnotationsScanner())
              .setUrls(ClasspathHelper.forClassLoader(Thread.currentThread().getContextClassLoader())));

      final ServiceContext serviceContext = new ServiceContextImpl(serviceManager, Collections.EMPTY_MAP);

      for (Class bootstrapService : bootstrapScanner.getTypesAnnotatedWith(NukeBootstrap.class)) {
         if (Service.class.isAssignableFrom(bootstrapService)) {
            LOG.info("Initializing bootstrap service: " + bootstrapService.getName());

            try {
               final Service serviceInstance = (Service) bootstrapService.newInstance();
               serviceManager.register(new InstanceContextImpl<Service>(LocalInstanceEnvironment.getInstance(), serviceInstance));

               serviceInstance.init(serviceContext);
            } catch (Exception ex) {
               LOG.error("Failed to load bootstrap service. This may cause unexpected behavior however the container may still attempt normal init.", ex);
            }
         }
      }
   }
}
