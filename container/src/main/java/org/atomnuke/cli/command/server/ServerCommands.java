package org.atomnuke.cli.command.server;

import org.atomnuke.cli.CliConfigurationHandler;
import org.atomnuke.util.cli.command.AbstractCommandList;

/**
 *
 * @author zinic
 */
public class ServerCommands extends AbstractCommandList {

   public ServerCommands(CliConfigurationHandler configurationHandler) {
      super(new Start(configurationHandler), new Stop(configurationHandler));
   }

   @Override
   public String getCommandToken() {
      return "server";
   }

   @Override
   public String getCommandDescription() {
      return "Nuke server commands.";
   }
}
