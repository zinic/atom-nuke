package org.atomnuke.fallout;

import org.atomnuke.service.ServiceUnavailableException;
import org.atomnuke.util.service.ServiceHandler;
import java.io.File;
import javax.xml.bind.JAXBException;
import org.atomnuke.Nuke;
import org.atomnuke.NukeEnvironment;
import org.atomnuke.NukeKernel;
import org.atomnuke.config.model.ServerConfiguration;
import org.atomnuke.container.boot.ContainerBootstrap;
import org.atomnuke.fallout.config.server.ServerConfigurationFileManager;
import org.atomnuke.fallout.context.config.ConfigurationContextUpdateListener;
import org.atomnuke.container.packaging.loader.PackageLoader;
import org.atomnuke.plugin.proxy.japi.JapiProxyFactory;
import org.atomnuke.service.ServiceManager;
import org.atomnuke.service.RuntimeServiceManager;
import org.atomnuke.service.gc.ReclamationHandler;
import org.atomnuke.task.manager.impl.GenericTaskManger;
import org.atomnuke.task.manager.service.TaskingService;
import org.atomnuke.task.threading.ExecutionManager;
import org.atomnuke.task.threading.ExecutionManagerImpl;
import org.atomnuke.task.threading.ExecutionQueueImpl;
import org.atomnuke.util.config.ConfigurationException;
import org.atomnuke.util.config.io.ConfigurationManager;
import org.atomnuke.util.config.update.ConfigurationContext;
import org.atomnuke.util.config.update.ConfigurationUpdateManager;
import org.atomnuke.util.config.update.listener.ConfigurationListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class NukeContainer {

   private static final Logger LOG = LoggerFactory.getLogger(NukeContainer.class);

   private final NukeEnvironment nukeEnvironment;
   private final ServiceManager serviceManager;

   private ConfigurationListener<ServerConfiguration> contextCfgListener;
   private Nuke nukeInstance;

   public NukeContainer(NukeEnvironment nukeEnvironment) {
      this.nukeEnvironment = nukeEnvironment;

      serviceManager = new RuntimeServiceManager(nukeEnvironment, new JapiProxyFactory());
   }

   public Nuke nukeInstance() {
      return nukeInstance;
   }

   public void start() {
      final long initTime = System.currentTimeMillis();

      LOG.info("Starting the Nuke container, Fallout.");

      LOG.debug("Bootstrapping the container.");
      new ContainerBootstrap(nukeEnvironment, serviceManager).bootstrap();

      LOG.debug("Kernel thread start.");
      initNuke();

      LOG.debug("Building context manager.");
      buildContextManager();

      LOG.debug("Registering Fallout configuration Sink.");
      registerConfigurationSinks();

      LOG.info("Nuke container started. Elapsed start-up time: " + (System.currentTimeMillis() - initTime) + "ms.");
   }

   private void initNuke() {
      try {
         final ReclamationHandler reclamationHandler = ServiceHandler.instance().firstAvailable(serviceManager, ReclamationHandler.class);
         final TaskingService taskingService = ServiceHandler.instance().firstAvailable(serviceManager, TaskingService.class);

         final ExecutionManager executionManager = new ExecutionManagerImpl(new ExecutionQueueImpl(nukeEnvironment));
         nukeInstance = new NukeKernel(nukeEnvironment, executionManager, reclamationHandler, new GenericTaskManger(executionManager, taskingService.taskTracker()), taskingService.tasker());
      } catch (ServiceUnavailableException sue) {
         throw new FalloutInitException(sue);
      }

      nukeInstance.shutdownHook().enlist(serviceManager);
      nukeInstance.start();
   }

   private void registerNukeCfgSink(ConfigurationUpdateManager cfgUpdateManager) throws FalloutInitException {
      try {
         final ConfigurationManager<ServerConfiguration> cfgManager = new ServerConfigurationFileManager(new File(nukeEnvironment.configurationLocation()));
         final ConfigurationContext<ServerConfiguration> configurationContext = cfgUpdateManager.register("Fallout File CFG", cfgManager);

         configurationContext.addListener(contextCfgListener);
      } catch (JAXBException jaxbe) {
         LOG.error(jaxbe.getMessage(), jaxbe);
         throw new FalloutInitException(jaxbe);
      } catch (ConfigurationException ce) {
         LOG.error(ce.getMessage(), ce);
         throw new FalloutInitException(ce);
      }
   }

   private void buildContextManager() {
      try {
         final PackageLoader firstLoader = ServiceHandler.instance().firstAvailable(serviceManager, PackageLoader.class);
         contextCfgListener = new ConfigurationContextUpdateListener(serviceManager, firstLoader.packageContexts(), nukeInstance);
      } catch (ServiceUnavailableException sue) {
         LOG.error(sue.getMessage(), sue);
      } catch (Exception ex) {
         LOG.error("Failed building context manager. Reason: " + ex.getMessage(), ex);
      }
   }

   private void registerConfigurationSinks() {
      try {
         final ConfigurationUpdateManager cfgUpdateManager = ServiceHandler.instance().firstAvailable(serviceManager, ConfigurationUpdateManager.class);
         registerNukeCfgSink(cfgUpdateManager);
      } catch (ServiceUnavailableException sue) {
         LOG.error(sue.getMessage(), sue);
      }
   }
}
