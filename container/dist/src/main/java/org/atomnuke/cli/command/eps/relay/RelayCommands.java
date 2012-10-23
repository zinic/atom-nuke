package org.atomnuke.cli.command.eps.relay;

import org.atomnuke.cli.CliConfigurationHandler;
import org.atomnuke.config.model.Relay;
import org.atomnuke.util.cli.command.AbstractCommandList;
import org.atomnuke.util.cli.command.result.CommandResult;
import org.atomnuke.util.cli.command.result.MessageResult;

/**
 *
 * @author zinic
 */
public class RelayCommands extends AbstractCommandList {

   public RelayCommands(CliConfigurationHandler configurationHandler) {
      super(configurationHandler, new AddRelay(configurationHandler), new DeleteRelay(configurationHandler));
   }

   @Override
   public CommandResult perform() throws Exception {
      final CliConfigurationHandler cfgHandler = getConfigHandler();
      final StringBuilder output = new StringBuilder();

      for (Relay relay : cfgHandler.getRelays()) {
         output.append(relay.getId());
      }

      return new MessageResult(output.toString());
   }

   @Override
   public String getCommandToken() {
      return "relays";
   }

   @Override
   public String getCommandDescription() {
      return "Eventlet relay commands.";
   }
}
