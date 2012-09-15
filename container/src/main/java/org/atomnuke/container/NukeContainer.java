package org.atomnuke.container;

import java.io.File;
import javax.xml.bind.JAXBException;
import org.atomnuke.Nuke;
import org.atomnuke.NukeEnv;
import org.atomnuke.NukeKernel;
import org.atomnuke.bindings.BindingLoaderException;
import org.atomnuke.bindings.loader.DirectoryLoaderManager;
import org.atomnuke.bindings.resolver.BindingResolver;
import org.atomnuke.bindings.resolver.BindingResolverImpl;
import org.atomnuke.config.model.ServerConfiguration;
import org.atomnuke.config.server.ServerConfigurationManager;
import org.atomnuke.container.context.ContextManager;
import org.atomnuke.util.config.ConfigurationException;
import org.atomnuke.util.config.io.ConfigurationManager;
import org.atomnuke.util.config.update.ConfigurationContext;
import org.atomnuke.util.config.update.ConfigurationUpdateManager;
import org.atomnuke.util.config.update.ConfigurationUpdateManagerImpl;
import org.atomnuke.util.config.update.ConfigurationUpdateRunnable;
import org.atomnuke.util.thread.Poller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class NukeContainer {

   private static final Logger LOG = LoggerFactory.getLogger(NukeContainer.class);
   private static final long DEFAULT_CFG_POLL_TIME_MS = 15000;

   private final ConfigurationUpdateManager configurationUpdateManager;
   private final NukeKernel nukeInstance;
   private final Poller cfgPoller;

   private ContextManager contextManager;

   public NukeContainer() {
      this(new NukeKernel());
   }

   public NukeContainer(NukeKernel nukeInstance) {
      this.nukeInstance = nukeInstance;

      configurationUpdateManager = new ConfigurationUpdateManagerImpl();
      cfgPoller = new Poller("Nuke Container - Configuration Poller", new ConfigurationUpdateRunnable(configurationUpdateManager), DEFAULT_CFG_POLL_TIME_MS);
   }

   public void start() {
      final long initTime = System.currentTimeMillis();

      LOG.info("Starting Nuke container...");
      LOG.debug("Building loader manager.");

      final BindingResolver bindingsResolver = BindingResolverImpl.defaultResolver();
      final DirectoryLoaderManager loaderManager = new DirectoryLoaderManager(NukeEnv.NUKE_LIB, bindingsResolver.registeredBindingContexts());

      try {
         LOG.debug("Loader manager starting.");

         loaderManager.load();
      } catch (BindingLoaderException ble) {
         LOG.debug("An error occured while loading bindings. Reason: " + ble.getMessage(), ble);

         throw new ContainerInitException(ble);
      }

      LOG.debug("Building context manager.");

      contextManager = new ContextManager(bindingsResolver, nukeInstance);

      try {
         final ConfigurationManager<ServerConfiguration> cfgManager = new ServerConfigurationManager(new File(NukeEnv.NUKE_HOME, NukeEnv.CONFIG_NAME));
         final ConfigurationContext<ServerConfiguration> configurationContext = configurationUpdateManager.register("org.atomnuke.container.cfg", cfgManager);

         configurationContext.addListener(contextManager);
      } catch (JAXBException jaxbe) {
         LOG.error(jaxbe.getMessage(), jaxbe);
         throw new ContainerInitException(jaxbe);
      } catch (ConfigurationException ce) {
         LOG.error(ce.getMessage(), ce);
         throw new ContainerInitException(ce);
      }

      LOG.debug("Kickstarting bootstrap threads.");

      cfgPoller.start();

      nukeInstance.addShutdownTask(new Runnable() {

         @Override
         public void run() {
            cfgPoller.destroy();
         }
      });

      nukeInstance.start();

      LOG.info("Nuke container started. Elapsed start-up time: " + (System.currentTimeMillis() - initTime) + "ms.");
   }
}
