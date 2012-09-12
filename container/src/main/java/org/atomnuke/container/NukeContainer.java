package org.atomnuke.container;

import java.io.File;
import javax.xml.bind.JAXBException;
import org.atomnuke.Nuke;
import org.atomnuke.NukeEnv;
import org.atomnuke.NukeKernel;
import org.atomnuke.bindings.BindingInstantiationException;
import org.atomnuke.bindings.BindingLoaderException;
import org.atomnuke.bindings.loader.DirectoryLoaderManager;
import org.atomnuke.bindings.resolver.BindingResolver;
import org.atomnuke.bindings.resolver.BindingResolverImpl;
import org.atomnuke.cli.command.server.builder.ServerBuilder;
import org.atomnuke.config.server.ServerConfigurationHandler;
import org.atomnuke.config.model.ServerConfiguration;
import org.atomnuke.config.server.ServerConfigurationManager;
import org.atomnuke.util.config.ConfigurationException;
import org.atomnuke.util.config.io.ConfigurationManager;
import org.atomnuke.util.config.update.ConfigurationContext;
import org.atomnuke.util.config.update.ConfigurationUpdateManager;
import org.atomnuke.util.config.update.ConfigurationUpdateManagerImpl;
import org.atomnuke.util.config.update.listener.ConfigurationListener;
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
   private final Thread cfgPollerThread;
   private final Nuke nukeInstance;
   private final Poller cfgPoller;

   public NukeContainer() {
      this(new NukeKernel());
   }

   public NukeContainer(Nuke nukeInstance) {
      this.nukeInstance = nukeInstance;

      final ConfigurationUpdateManagerImpl newUpdateManagerImpl = new ConfigurationUpdateManagerImpl();
      configurationUpdateManager = new ConfigurationUpdateManagerImpl();

      cfgPoller = new Poller(newUpdateManagerImpl, DEFAULT_CFG_POLL_TIME_MS);
      this.cfgPollerThread = new Thread(cfgPoller, "Nuke Container - Configuration Poller");
   }

   public void start(ServerConfigurationHandler cfgHandler) {
      final long initTime = System.currentTimeMillis();

      LOG.info("Starting Nuke container...");

      final BindingResolver bindingsResolver = BindingResolverImpl.defaultResolver();
      final DirectoryLoaderManager loaderManager = new DirectoryLoaderManager(NukeEnv.NUKE_LIB, bindingsResolver.registeredBindingContexts());

      try {
         loaderManager.load();
      } catch (BindingLoaderException ble) {
         LOG.error("An error occured while loading bindings. Reason: " + ble.getMessage(), ble);

         throw new ContainerInitException(ble);
      }

      cfgPollerThread.start();
      nukeInstance.start();

      try {
         final ConfigurationManager<ServerConfiguration> cfgManager = new ServerConfigurationManager(new File(NukeEnv.NUKE_HOME, NukeEnv.CONFIG_NAME));
         final ConfigurationContext<ServerConfiguration> configurationContext = configurationUpdateManager.register("org.atomnuke.container.cfg", cfgManager);

         configurationContext.addListener(new ConfigurationListener<ServerConfiguration>() {
            @Override
            public void updated(ServerConfiguration configuration) throws ConfigurationException {
               final ServerBuilder serverBuilder = new ServerBuilder(new ServerConfigurationHandler(cfgManager, configuration), bindingsResolver);

               try {
                  serverBuilder.build(nukeInstance);
               } catch (BindingInstantiationException bie) {
                  LOG.error("An error occured while initializing bindings. Reason: " + bie.getMessage(), bie);

                  throw new ContainerInitException(bie);
               } catch (ConfigurationException ce) {
                  LOG.error("An error occured while handling the configuration. Reason: " + ce.getMessage(), ce);

                  throw new ContainerInitException(ce);
               }
            }
         });
      } catch (JAXBException jaxbe) {
         LOG.error(jaxbe.getMessage(), jaxbe);
      } catch (ConfigurationException ce) {
         LOG.error(ce.getMessage(), ce);
      }

      LOG.info("Nuke container started. Elapsed start-up time: " + (System.currentTimeMillis() - initTime) + "ms.");
   }
}
