package net.jps.nuke.cli.command.server;

import net.jps.nuke.config.ConfigurationReader;
import net.jps.nuke.util.cli.command.AbstractCommandList;

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
