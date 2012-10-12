package org.atomnuke.container.service.config;

import org.atomnuke.container.service.annotation.NukeService;
import org.atomnuke.service.Service;
import org.atomnuke.service.context.ServiceContext;
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
@NukeService
public class ConfigurationService implements Service {

   public static String CFG_POLLER_PROPERTY_KEY = "org.atomnuke.container.service.config.ConfigurationService.poll_interval_ms";
   public static String CFG_SERVICE_NAME = "org.atomnuke.container.service.config.ConfigurationService";

   private static final long DEFAULT_CFG_POLL_TIME_MS = 15000;
   private static final Logger LOG = LoggerFactory.getLogger(ConfigurationService.class);

   private final ConfigurationUpdateManager cfgUpdateMangaer;
   private Poller cfgPoller;

   public ConfigurationService() {
      cfgUpdateMangaer = new ConfigurationUpdateManagerImpl();
   }

   @Override
   public Object instance() {
      return cfgUpdateMangaer;
   }

   @Override
   public String name() {
      return CFG_SERVICE_NAME;
   }

   @Override
   public boolean provides(Class serviceInterface) {
      return serviceInterface.isAssignableFrom(cfgUpdateMangaer.getClass());
   }

   @Override
   public void init(ServiceContext sc) {
      long pollerTime = DEFAULT_CFG_POLL_TIME_MS;

      if (sc.parameters().containsKey(CFG_POLLER_PROPERTY_KEY)) {
         final String configuredPollTime = sc.parameters().get(CFG_POLLER_PROPERTY_KEY);

         try {
            pollerTime = Long.parseLong(configuredPollTime);
         } catch (NumberFormatException nfe) {
            LOG.error("Value: " + configuredPollTime + " is not a valid number. The configuration poller accepts time periods in ms.", nfe);
         }
      }

      LOG.info("Nuke configuration poller starting.");

      cfgPoller = new Poller("Nuke Container - Configuration Poller", new ConfigurationUpdateRunnable(cfgUpdateMangaer), pollerTime);
      cfgPoller.start();
   }

   @Override
   public void destroy() {
      LOG.info("Nuke configuration poller stopping.");

      try {
         cfgPoller.haltPolling();
         cfgPoller.join();
      } catch (InterruptedException ie) {
         LOG.error("Interrupted while waiting for the cfg poller thread to exit. Attempting to kill thread and exit.", ie);

         cfgPoller.interrupt();
      }
   }
}
