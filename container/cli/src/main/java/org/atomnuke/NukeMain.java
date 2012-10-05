package org.atomnuke;

import java.io.File;
import org.atomnuke.cli.command.Root;
import org.atomnuke.cli.CliConfigurationHandler;
import org.atomnuke.config.model.ServerConfiguration;
import org.atomnuke.config.server.ServerConfigurationManager;
import org.atomnuke.util.cli.CommandDriver;
import org.atomnuke.util.cli.command.Command;
import org.atomnuke.util.cli.command.result.CommandResult;
import org.atomnuke.util.config.io.ConfigurationManager;

/**
 *
 * @author zinic
 */
public final class NukeMain {

   private NukeMain() {
   }

   public static void main(String[] args) throws Exception {
      final ConfigurationManager<ServerConfiguration> cfgManager = new ServerConfigurationManager(new File(NukeEnv.NUKE_HOME, NukeEnv.CONFIG_NAME));
      final ServerConfiguration previousCfg = cfgManager.read();

      final CliConfigurationHandler handler = new CliConfigurationHandler(cfgManager, previousCfg != null ? previousCfg : new ServerConfiguration());

      final Command rootCommand = new Root(handler);

      final CommandResult result = new CommandDriver(rootCommand, args).go();
      final String stringResult = result.getStringResult();

      if (stringResult != null && stringResult.length() > 0) {
         System.out.println(result.getStringResult());
      }

      if (result.shouldExit()) {
         System.exit(result.getStatusCode());
      }
   }
}
