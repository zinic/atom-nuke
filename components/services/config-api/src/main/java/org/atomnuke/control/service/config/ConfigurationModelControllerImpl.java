package org.atomnuke.control.service.config;

import org.atomnuke.config.model.ServerConfiguration;
import org.atomnuke.util.config.update.ConfigurationContext;
import org.atomnuke.util.config.update.ConfigurationUpdateManager;

/**
 *
 * @author zinic
 */
public class ConfigurationModelControllerImpl {

   private final ServerConfiguration serverModel;
   private ConfigurationContext<ServerConfiguration> cfgContext;

   public ConfigurationModelControllerImpl(ServerConfiguration serverModel) {
      this.serverModel = serverModel;
   }

   public void registerToUpdateManager(ConfigurationUpdateManager updateManager) {
      cfgContext = updateManager.register("org.atomnuke.config.fallout.ApiManagedServerConfiguration", null);
   }
}
