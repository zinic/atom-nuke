package org.atomnuke.cli;

import org.atomnuke.atombus.config.model.ServerConfiguration;
import org.atomnuke.util.config.ConfigurationException;
import org.atomnuke.fallout.config.server.ServerConfigurationHandler;
import org.atomnuke.util.config.io.ConfigurationManager;

/**
 *
 * @author zinic
 */
public class CliConfigurationHandler extends ServerConfigurationHandler {

   private final ConfigurationManager<ServerConfiguration> configurationManager;

   public CliConfigurationHandler(ConfigurationManager<ServerConfiguration> configurationManager, ServerConfiguration configurationCopy) {
      super(configurationCopy);
      this.configurationManager = configurationManager;
   }

   public void write() throws ConfigurationException {
      configurationManager.write(getConfiguration());
   }
}
