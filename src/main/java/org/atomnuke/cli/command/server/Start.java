package org.atomnuke.cli.command.server;

import org.atomnuke.cli.command.AbstractNukeCommand;
import org.atomnuke.config.ConfigurationReader;
import org.atomnuke.util.cli.command.result.CommandResult;
import org.atomnuke.util.cli.command.result.CommandSuccess;

/**
 *
 * @author zinic
 */
public class Start extends AbstractNukeCommand {

   public Start(ConfigurationReader configurationReader) {
      super(configurationReader);
   }

   @Override
   public String getCommandToken() {
      return "start";
   }

   @Override
   public String getCommandDescription() {
      return "Starts a new nuke instance.";
   }

   @Override
   public CommandResult perform(String[] arguments) throws Exception {
      return new CommandSuccess();
   }
}
