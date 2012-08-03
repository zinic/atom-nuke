package net.jps.nuke.cli.command.server;

import net.jps.nuke.cli.command.AbstractNukeCommand;
import net.jps.nuke.config.ConfigurationReader;
import net.jps.nuke.util.cli.command.result.CommandResult;
import net.jps.nuke.util.cli.command.result.SuccessResult;

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
      return new SuccessResult();
   }
}
