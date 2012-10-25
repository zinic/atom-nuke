package org.atomnuke.cli.command.eps.relay;

import org.atomnuke.cli.CliConfigurationHandler;
import org.atomnuke.cli.command.AbstractNukeCommand;
import org.atomnuke.config.model.Relay;
import org.atomnuke.util.cli.command.result.CommandFailure;
import org.atomnuke.util.cli.command.result.CommandResult;
import org.atomnuke.util.cli.command.result.CommandSuccess;

/**
 *
 * @author zinic
 */
public class AddRelay extends AbstractNukeCommand {

   private static final int RELAY_ID = 0;

   public AddRelay(CliConfigurationHandler configurationHandler) {
      super(configurationHandler);
   }

   @Override
   public String getCommandToken() {
      return "add";
   }

   @Override
   public String getCommandDescription() {
      return "Adds a new relay.";
   }

   @Override
   public CommandResult perform(String[] arguments) throws Exception {
      if (arguments.length != 1) {
         return new CommandFailure("Adding a relay requires one arguments: <relay-id>");
      }
      final CliConfigurationHandler cfgHandler = getConfigHandler();

      if (cfgHandler.findRelay(arguments[RELAY_ID]) != null) {
         return new CommandFailure("A relay with the id \"" + arguments[RELAY_ID] + "\" already exists.");
      }

      final Relay newRelay = new Relay();
      newRelay.setId(arguments[RELAY_ID]);

      cfgHandler.getRelays().add(newRelay);
      cfgHandler.write();

      return new CommandSuccess();
   }
}
