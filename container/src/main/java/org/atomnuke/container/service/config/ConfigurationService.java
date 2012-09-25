package org.atomnuke.container.service.config;

import org.atomnuke.service.Service;
import org.atomnuke.container.service.annotation.NukeService;
import org.atomnuke.service.context.ServiceContext;
import org.atomnuke.util.config.update.ConfigurationUpdateManager;
import org.atomnuke.util.config.update.ConfigurationUpdateManagerImpl;
import org.atomnuke.util.config.update.ConfigurationUpdateRunnable;

import org.atomnuke.util.thread.Poller;

/**
 *
 * @author zinic
 */
@NukeService
public class ConfigurationService implements Service {

   private static final long DEFAULT_CFG_POLL_TIME_MS = 15000;

   private final ConfigurationUpdateManager configurationUpdateManager;
   private final Poller cfgPoller;

   public ConfigurationService() {
      configurationUpdateManager = new ConfigurationUpdateManagerImpl();
      cfgPoller = new Poller("Nuke Container - Configuration Poller", new ConfigurationUpdateRunnable(configurationUpdateManager), DEFAULT_CFG_POLL_TIME_MS);
   }

   @Override
   public String name() {
      return "Configuration Service";
   }

   @Override
   public void init(ServiceContext sc) {
      cfgPoller.start();
   }

   @Override
   public void destroy() {
      cfgPoller.haltPolling();

      try {
         cfgPoller.join();
      } catch (InterruptedException ie) {
         // TODO:LOG
      }
   }

   @Override
   public Object instance() {
      return configurationUpdateManager;
   }
}
