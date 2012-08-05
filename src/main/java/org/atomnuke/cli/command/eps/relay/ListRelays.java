package org.atomnuke.cli.command.eps.relay;

import org.atomnuke.cli.command.AbstractNukeCommand;
import org.atomnuke.config.ConfigurationHandler;
import org.atomnuke.config.ConfigurationReader;
import org.atomnuke.config.model.Relay;
import org.atomnuke.util.cli.command.result.CommandResult;
import org.atomnuke.util.cli.command.result.MessageResult;

/**
 *
 * @author zinic
 */
public class ListRelays extends AbstractNukeCommand {

   public ListRelays(ConfigurationReader configurationReader) {
      super(configurationReader);
   }

   @Override
   public String getCommandToken() {
      return "list";
   }

   @Override
   public String getCommandDescription() {
      return "Lists all registered relays.";
   }

   @Override
   public CommandResult perform(String[] arguments) throws Exception {
      final ConfigurationHandler cfgHandler = getConfigurationReader().readConfiguration();
      final StringBuilder output = new StringBuilder();

      for (Relay relay : cfgHandler.getRelays()) {
         output.append(relay.getId());
      }

      return new MessageResult(output.toString());
   }
}
