package org.atomnuke.cli.command.eps.relay;

import org.atomnuke.cli.CliConfigurationHandler;
import org.atomnuke.util.cli.command.AbstractCommandList;

/**
 *
 * @author zinic
 */
public class RelayCommands extends AbstractCommandList {

   public RelayCommands(CliConfigurationHandler configurationHandler) {
      super(new AddRelay(configurationHandler), new DeleteRelay(configurationHandler), new ListRelays(configurationHandler));
   }

   @Override
   public String getCommandToken() {
      return "relay";
   }

   @Override
   public String getCommandDescription() {
      return "Eventlet relay commands.";
   }
}
