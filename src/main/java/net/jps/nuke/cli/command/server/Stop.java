package net.jps.nuke.cli.command.server;

import net.jps.nuke.cli.command.AbstractNukeCommand;
import net.jps.nuke.config.ConfigurationReader;
import net.jps.nuke.util.cli.command.result.CommandResult;
import net.jps.nuke.util.cli.command.result.SuccessResult;

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
      return "Stops the currently running nuke instance.";
   }

   @Override
   public CommandResult perform(String[] arguments) throws Exception {
      return new SuccessResult();
   }
}
