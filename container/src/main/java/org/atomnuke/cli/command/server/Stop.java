package org.atomnuke.cli.command.server;

import org.atomnuke.cli.command.AbstractNukeCommand;
import org.atomnuke.config.server.ServerConfigurationHandler;
import org.atomnuke.util.cli.command.result.CommandResult;
import org.atomnuke.util.cli.command.result.CommandSuccess;

/**
 *
 * @author zinic
 */
public class Stop extends AbstractNukeCommand {

   public Stop(ServerConfigurationHandler configurationHandler) {
      super(configurationHandler);
   }

   @Override
   public String getCommandToken() {
      return "stop";
   }

   @Override
   public String getCommandDescription() {
      return "[NOT IMPLEMENTED] Stops the currently running nuke instance. [NOT IMPLEMENTED]";
   }

   @Override
   public CommandResult perform(String[] arguments) throws Exception {
      return new CommandSuccess();
   }
}
