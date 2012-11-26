package org.atomnuke.container.service.config;

import org.atomnuke.util.config.update.ConfigurationUpdateService;
import org.atomnuke.util.lifecycle.runnable.ReclaimableTaskPartial;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class ConfigurationUpdateRunnable extends ReclaimableTaskPartial {

   private static final Logger LOG = LoggerFactory.getLogger(ConfigurationUpdateRunnable.class);

   private final ConfigurationUpdateService configurationUpdateManager;

   public ConfigurationUpdateRunnable(ConfigurationUpdateService configurationUpdateManager) {
      this.configurationUpdateManager = configurationUpdateManager;
   }

   @Override
   public void destroy() {
      configurationUpdateManager.destroy();
   }

   @Override
   public void run() {
      LOG.debug("Performing configuration update polling.");
      configurationUpdateManager.update();
   }
}
