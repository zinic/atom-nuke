package org.atomnuke.container.service.config;

import org.atomnuke.task.ReclaimableRunnable;
import org.atomnuke.util.config.update.ConfigurationUpdateManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class ConfigurationUpdateRunnable implements ReclaimableRunnable {

   private static final Logger LOG = LoggerFactory.getLogger(ConfigurationUpdateRunnable.class);

   private final ConfigurationUpdateManager configurationUpdateManager;

   public ConfigurationUpdateRunnable(ConfigurationUpdateManager configurationUpdateManager) {
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
