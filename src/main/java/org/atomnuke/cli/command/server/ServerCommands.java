package org.atomnuke.cli.command.server;

import org.atomnuke.config.ConfigurationReader;
import org.atomnuke.util.cli.command.AbstractCommandList;

/**
 *
 * @author zinic
 */
public class ServerCommands extends AbstractCommandList {

   public ServerCommands(ConfigurationReader configurationReader) {
      super(new Start(configurationReader), new Stop(configurationReader));
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
