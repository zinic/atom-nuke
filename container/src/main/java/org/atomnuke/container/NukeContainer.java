package org.atomnuke.container;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import javax.xml.bind.JAXBException;
import org.atomnuke.Nuke;
import org.atomnuke.NukeEnv;
import org.atomnuke.NukeKernel;
import org.atomnuke.config.model.ServerConfiguration;
import org.atomnuke.container.config.ServerConfigurationManager;
import org.atomnuke.container.context.ContextManager;
import org.atomnuke.container.packaging.loader.PackageLoader;
import org.atomnuke.container.service.annotation.NukeBootstrap;
import org.atomnuke.service.ServiceManager;
import org.atomnuke.service.ServiceManagerImpl;
import org.atomnuke.plugin.InstanceContextImpl;
import org.atomnuke.plugin.local.LocalInstanceEnvironment;
import org.atomnuke.service.Service;
import org.atomnuke.service.context.ServiceContext;
import org.atomnuke.service.context.ServiceContextImpl;
import org.atomnuke.util.config.ConfigurationException;
import org.atomnuke.util.config.io.ConfigurationManager;
import org.atomnuke.util.config.update.ConfigurationContext;
import org.atomnuke.util.config.update.ConfigurationUpdateManager;
import org.atomnuke.task.context.TaskContextImpl;
import org.atomnuke.task.lifecycle.InitializationException;
import org.atomnuke.task.lifecycle.TaskLifeCycle;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class NukeContainer {

   private static final Logger LOG = LoggerFactory.getLogger(NukeContainer.class);

   private final ServiceManager serviceManager;
   private final Nuke nukeInstance;

   private ContextManager contextManager;

   public NukeContainer() {
      this.serviceManager = new ServiceManagerImpl();
      this.nukeInstance = new NukeKernel();
   }

   public Nuke nukeInstance() {
      return nukeInstance;
   }

   public void init(TaskLifeCycle tlc) throws InitializationException {
      init(tlc, Collections.EMPTY_MAP);
   }

   public void init(TaskLifeCycle tlc, Map<String, String> params) throws InitializationException {
      tlc.init(new TaskContextImpl(params, serviceManager, nukeInstance.tasker()));
   }

   private static void bootstrap(ServiceManager serviceManager) {
      final Reflections bootstrapScanner = new Reflections(new ConfigurationBuilder()
              .setScanners(new SubTypesScanner(), new TypeAnnotationsScanner())
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
               LOG.error("Failed to load bootstrap service. This may cause unexpected behavior however the container will still attempt normal init.", ex);
            }
         }
      }
   }

   public void start() {
      final long initTime = System.currentTimeMillis();

      LOG.info("Starting the Fallout container.");

      LOG.debug("Bootstrapping the container.");
      bootstrap(serviceManager);

      LOG.debug("Building context manager.");
      buildContextManager();

      LOG.debug("Registering Fallout configuration listener.");
      registerConfigurationListeners();

      LOG.debug("Kernel thread start.");

      nukeInstance.shutdownHook().enlist(serviceManager);
      nukeInstance.start();

      LOG.info("Nuke container started. Elapsed start-up time: " + (System.currentTimeMillis() - initTime) + "ms.");
   }

   private void registerNukeCfgListener(ConfigurationUpdateManager cfgUpdateManager) throws ContainerInitException {
      try {
         final ConfigurationManager<ServerConfiguration> cfgManager = new ServerConfigurationManager(new File(NukeEnv.NUKE_HOME, NukeEnv.CONFIG_NAME));
         final ConfigurationContext<ServerConfiguration> configurationContext = cfgUpdateManager.register("org.atomnuke.container.cfg", cfgManager);

         configurationContext.addListener(contextManager);
      } catch (JAXBException jaxbe) {
         LOG.error(jaxbe.getMessage(), jaxbe);
         throw new ContainerInitException(jaxbe);
      } catch (ConfigurationException ce) {
         LOG.error(ce.getMessage(), ce);
         throw new ContainerInitException(ce);
      }
   }

   private void buildContextManager() {
      final Collection<String> packageLoaderServices = serviceManager.listRegisteredServicesFor(PackageLoader.class);

      if (packageLoaderServices.isEmpty()) {
         LOG.error("No package loader service available. Expected service interface is: " + PackageLoader.class.getName());
         return;
      }

      for (String availablePackageLoader : packageLoaderServices) {
         try {
            final PackageLoader loader = serviceManager.get(availablePackageLoader, PackageLoader.class);
            contextManager = new ContextManager(serviceManager, loader.packageContexts(), nukeInstance);

            break;
         } catch (Exception ex) {
            LOG.error("Failed using package loader: " + availablePackageLoader + " - Reason: " + ex.getMessage(), ex);
         }
      }
   }

   private void registerConfigurationListeners() {
      final Collection<String> configurationServices = serviceManager.listRegisteredServicesFor(ConfigurationUpdateManager.class);

      if (configurationServices.isEmpty()) {
         LOG.error("No configuration service available. Expected service interface is: " + ConfigurationUpdateManager.class.getName());
         return;
      }

      for (String availableCfgService : configurationServices) {
         try {
            final ConfigurationUpdateManager cfgUpdateManager = serviceManager.get(availableCfgService, ConfigurationUpdateManager.class);
            registerNukeCfgListener(cfgUpdateManager);

            break;
         } catch (Exception ex) {
            LOG.error("Failed using configuration service: " + availableCfgService + " - Reason: " + ex.getMessage(), ex);
         }
      }
   }
}
