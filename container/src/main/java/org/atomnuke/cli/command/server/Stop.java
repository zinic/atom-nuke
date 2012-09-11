package org.atomnuke.cli.command.server;

import org.atomnuke.cli.command.AbstractNukeCommand;
import org.atomnuke.config.ConfigurationReader;
import org.atomnuke.util.cli.command.result.CommandResult;
import org.atomnuke.util.cli.command.result.CommandSuccess;

/**
 *
 * @author zinic
 */
public class Stop extends AbstractNukeCommand {

   public Stop(ConfigurationReader configurationReader) {
      super(configurationReader);
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
