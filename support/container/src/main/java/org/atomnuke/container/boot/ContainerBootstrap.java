package org.atomnuke.container.boot;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.atomnuke.NukeEnvironment;
import org.atomnuke.container.service.annotation.NukeBootstrap;
import org.atomnuke.plugin.context.InstanceContextImpl;
import org.atomnuke.plugin.env.NopInstanceEnvironment;
import org.atomnuke.service.Service;
import org.atomnuke.service.ServiceManager;
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

   private final NukeEnvironment nukeEnvironment;
   private final ServiceManager serviceManager;

   public ContainerBootstrap(NukeEnvironment nukeEnvironment, ServiceManager serviceManager) {
      this.nukeEnvironment = nukeEnvironment;
      this.serviceManager = serviceManager;
   }

   @Override
   public void bootstrap() {
      final ExecutorService bootStrapExecutorService = new ThreadPoolExecutor(nukeEnvironment.numProcessors(), nukeEnvironment.numProcessors(),
              5, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

      final Reflections bootstrapScanner = new Reflections(new ConfigurationBuilder()
              .setScanners(new TypeAnnotationsScanner())
              .setExecutorService(bootStrapExecutorService)
              .setUrls(ClasspathHelper.forClassLoader(Thread.currentThread().getContextClassLoader(), ClassLoader.getSystemClassLoader())));

      for (Class bootstrapService : bootstrapScanner.getTypesAnnotatedWith(NukeBootstrap.class)) {
         if (Service.class.isAssignableFrom(bootstrapService)) {
            LOG.debug("Submitting bootstrap service: " + bootstrapService.getName());

            try {
               final Service serviceInstance = (Service) bootstrapService.newInstance();
               serviceManager.submit(new InstanceContextImpl<Service>(NopInstanceEnvironment.getInstance(), serviceInstance));
            } catch (Exception ex) {
               LOG.error("Failed to load bootstrap service: " + bootstrapService.getName() + " - This may cause unexpected behavior however the container may still attempt normal init.", ex);
            }
         }
      }

      bootStrapExecutorService.shutdownNow();

      serviceManager.resolve();
   }
}
