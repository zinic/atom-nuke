package org.atomnuke.cli.command.eps;

import org.atomnuke.cli.command.eps.eventlet.EventletCommands;
import org.atomnuke.cli.command.eps.relay.RelayCommands;
import org.atomnuke.config.ConfigurationReader;
import org.atomnuke.util.cli.command.AbstractCommandList;

/**
 *
 * @author zinic
 */
public class EPSCommands extends AbstractCommandList {

   public EPSCommands(ConfigurationReader configurationReader) {
      super(new RelayCommands(configurationReader), new EventletCommands(configurationReader));
   }

   @Override
   public String getCommandToken() {
      return "eps";
   }

   @Override
   public String getCommandDescription() {
      return "Event processing system commands.";
   }
}
