package org.atomnuke.util.config.update;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class ConfigurationUpdateRunnable implements Runnable {

   private static final Logger LOG = LoggerFactory.getLogger(ConfigurationUpdateRunnable.class);

   private final ConfigurationUpdateManager configurationUpdateManager;

   public ConfigurationUpdateRunnable(ConfigurationUpdateManager configurationUpdateManager) {
      this.configurationUpdateManager = configurationUpdateManager;
   }

   @Override
   public void run() {
      LOG.debug("Performing configuration update polling.");
      
      configurationUpdateManager.update();
   }
}
