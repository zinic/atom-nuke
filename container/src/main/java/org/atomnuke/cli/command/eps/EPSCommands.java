package org.atomnuke.cli.command.eps;

import org.atomnuke.cli.command.eps.eventlet.EventletCommands;
import org.atomnuke.cli.command.eps.relay.RelayCommands;
import org.atomnuke.config.server.ServerConfigurationHandler;
import org.atomnuke.util.cli.command.AbstractCommandList;

/**
 *
 * @author zinic
 */
public class EPSCommands extends AbstractCommandList {

   public EPSCommands(ServerConfigurationHandler configurationHandler) {
      super(new RelayCommands(configurationHandler), new EventletCommands(configurationHandler));
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
