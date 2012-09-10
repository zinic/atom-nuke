package org.atomnuke.cli.command.eps.relay;

import org.atomnuke.config.ConfigurationReader;
import org.atomnuke.util.cli.command.AbstractCommandList;

/**
 *
 * @author zinic
 */
public class RelayCommands extends AbstractCommandList {

   public RelayCommands(ConfigurationReader configurationReader) {
      super(new AddRelay(configurationReader), new DeleteRelay(configurationReader), new ListRelays(configurationReader));
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
