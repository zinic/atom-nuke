package org.atomnuke.fallout;

import org.atomnuke.fallout.service.ServiceUnavailableException;
import org.atomnuke.fallout.service.ServiceHandler;
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
import org.atomnuke.container.service.gc.ReclaimationHandler;
import org.atomnuke.service.ServiceManager;
import org.atomnuke.service.ServiceManagerImpl;
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

   private final ServiceManager serviceManager;
   private final ServiceHandler serviceHelper;
   private final Nuke nukeInstance;

   private ContextManager contextManager;

   public NukeContainer() {
      this.serviceManager = new ServiceManagerImpl();
      this.serviceHelper = new ServiceHandler(serviceManager);
      this.nukeInstance = new NukeKernel();
   }

   public Nuke nukeInstance() {
      return nukeInstance;
   }

   public void start() {
      final long initTime = System.currentTimeMillis();

      LOG.info("Starting the Nuke container, Fallout.");

      LOG.debug("Bootstrapping the container.");
      new ContainerBootstrap(serviceManager).bootstrap();

      LOG.debug("Building reclaimation service.");
      buildReclaimationManager();

      LOG.debug("Building context manager.");
      buildContextManager();

      LOG.debug("Registering Fallout configuration listener.");
      registerConfigurationListeners();

      LOG.debug("Kernel thread start.");

      nukeInstance.shutdownHook().enlist(serviceManager);
      nukeInstance.start();

      LOG.info("Nuke container started. Elapsed start-up time: " + (System.currentTimeMillis() - initTime) + "ms.");
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

   private void buildContextManager() {
      // First package loader wins
      try {
         final PackageLoader firstLoader = serviceHelper.firstAvailable(PackageLoader.class);
         contextManager = new ContextManager(serviceManager, firstLoader.packageContexts(), nukeInstance);
      } catch (ServiceUnavailableException sue) {
         LOG.error(sue.getMessage(), sue);
      } catch (Exception ex) {
         LOG.error("Failed building context manager. Reason: " + ex.getMessage(), ex);
      }
   }

   private void buildReclaimationManager() {
      // First package loader wins
      try {
         final ReclaimationHandler firstReclaimationService = serviceHelper.firstAvailable(ReclaimationHandler.class);
         
      } catch (ServiceUnavailableException sue) {
         LOG.error(sue.getMessage(), sue);
      } catch (Exception ex) {
         LOG.error("Failed building context manager. Reason: " + ex.getMessage(), ex);
      }
   }

   private void registerConfigurationListeners() {
      try {
         final ConfigurationUpdateManager cfgUpdateManager = serviceHelper.firstAvailable(ConfigurationUpdateManager.class);
         registerNukeCfgListener(cfgUpdateManager);
      } catch (ServiceUnavailableException sue) {
         LOG.error(sue.getMessage(), sue);
      }
   }
}
