package org.atomnuke.fallout;

import org.atomnuke.service.ServiceUnavailableException;
import org.atomnuke.util.service.ServiceHandler;
import java.io.File;
import javax.xml.bind.JAXBException;
import org.atomnuke.Nuke;
import org.atomnuke.NukeEnv;
import org.atomnuke.NukeKernel;
import org.atomnuke.config.model.ServerConfiguration;
import org.atomnuke.container.boot.ContainerBootstrap;
import org.atomnuke.fallout.config.server.ServerConfigurationManager;
import org.atomnuke.fallout.context.ContextManager;
import org.atomnuke.container.packaging.loader.PackageLoader;
import org.atomnuke.plugin.proxy.japi.JapiProxyFactory;
import org.atomnuke.service.ServiceManager;
import org.atomnuke.service.RuntimeServiceManager;
import org.atomnuke.service.gc.ReclaimationHandler;
import org.atomnuke.task.manager.Tasker;
import org.atomnuke.task.threading.ExecutionManagerImpl;
import org.atomnuke.task.threading.ExecutionQueueImpl;
import org.atomnuke.util.config.ConfigurationException;
import org.atomnuke.util.config.io.ConfigurationManager;
import org.atomnuke.util.config.update.ConfigurationContext;
import org.atomnuke.util.config.update.ConfigurationUpdateManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class NukeContainer {

   private static final Logger LOG = LoggerFactory.getLogger(NukeContainer.class);
   private ServiceManager serviceManager;
   private ContextManager contextManager;
   private Nuke nukeInstance;

   public Nuke nukeInstance() {
      return nukeInstance;
   }

   public void start() {
      final long initTime = System.currentTimeMillis();

      LOG.info("Starting the Nuke container, Fallout.");

      LOG.debug("Bootstrapping the container.");
      serviceManager = new RuntimeServiceManager(new JapiProxyFactory());

      new ContainerBootstrap(serviceManager).bootstrap();

      final ServiceHandler serviceHandler = new ServiceHandler(serviceManager);

      LOG.debug("Kernel thread start.");
      initNuke(serviceHandler);

      LOG.debug("Building context manager.");
      buildContextManager(serviceHandler);

      LOG.debug("Registering Fallout configuration listener.");
      registerConfigurationListeners(serviceHandler);

      LOG.info("Nuke container started. Elapsed start-up time: " + (System.currentTimeMillis() - initTime) + "ms.");
   }

   private void initNuke(ServiceHandler serviceHandler) {
      try {
         final ReclaimationHandler reclaimationHandler = serviceHandler.firstAvailable(ReclaimationHandler.class);
         final Tasker tasker = serviceHandler.firstAvailable(Tasker.class);

         nukeInstance = new NukeKernel(new ExecutionManagerImpl(new ExecutionQueueImpl()), reclaimationHandler, tasker);
      } catch (ServiceUnavailableException sue) {
         throw new FalloutInitException(sue);
      }

      nukeInstance.shutdownHook().enlist(serviceManager);
      nukeInstance.start();
   }

   private void registerNukeCfgListener(ConfigurationUpdateManager cfgUpdateManager) throws FalloutInitException {
      try {
         final ConfigurationManager<ServerConfiguration> cfgManager = new ServerConfigurationManager(new File(NukeEnv.NUKE_HOME, NukeEnv.CONFIG_NAME));
         final ConfigurationContext<ServerConfiguration> configurationContext = cfgUpdateManager.register("org.atomnuke.container.cfg", cfgManager);

         configurationContext.addListener(contextManager);
      } catch (JAXBException jaxbe) {
         LOG.error(jaxbe.getMessage(), jaxbe);
         throw new FalloutInitException(jaxbe);
      } catch (ConfigurationException ce) {
         LOG.error(ce.getMessage(), ce);
         throw new FalloutInitException(ce);
      }
   }

   private void buildContextManager(ServiceHandler serviceHandler) {
      // First package loader wins
      try {
         final PackageLoader firstLoader = serviceHandler.firstAvailable(PackageLoader.class);
         contextManager = new ContextManager(serviceManager, firstLoader.packageContexts(), nukeInstance);
      } catch (ServiceUnavailableException sue) {
         LOG.error(sue.getMessage(), sue);
      } catch (Exception ex) {
         LOG.error("Failed building context manager. Reason: " + ex.getMessage(), ex);
      }
   }

   private void registerConfigurationListeners(ServiceHandler serviceHandler) {
      try {
         final ConfigurationUpdateManager cfgUpdateManager = serviceHandler.firstAvailable(ConfigurationUpdateManager.class);
         registerNukeCfgListener(cfgUpdateManager);
      } catch (ServiceUnavailableException sue) {
         LOG.error(sue.getMessage(), sue);
      }
   }
}
