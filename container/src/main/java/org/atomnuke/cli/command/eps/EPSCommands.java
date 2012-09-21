package org.atomnuke.cli.command.eps;

import org.atomnuke.cli.command.eps.eventlet.EventletCommands;
import org.atomnuke.cli.command.eps.relay.RelayCommands;
import org.atomnuke.cli.CliConfigurationHandler;
import org.atomnuke.util.cli.command.AbstractCommandList;

/**
 *
 * @author zinic
 */
public class EPSCommands extends AbstractCommandList {

   public EPSCommands(CliConfigurationHandler configurationHandler) {
      super(configurationHandler, new RelayCommands(configurationHandler), new EventletCommands(configurationHandler));
   }

   @Override
   public String getCommandToken() {
      return "eps";
   }

   @Override
   public String getCommandDescription() {
      return "Relay and eventlet commands.";
   }
}
