package org.atomnuke.cli.command.eps.eventlet;

import org.atomnuke.config.server.ServerConfigurationHandler;
import org.atomnuke.util.cli.command.AbstractCommandList;

/**
 *
 * @author zinic
 */
public class EventletCommands extends AbstractCommandList {

   public EventletCommands(ServerConfigurationHandler configurationHandler) {
      super(new AddEventlet(configurationHandler), new DeleteEventlet(configurationHandler), new ListEventlets(configurationHandler));
   }

   @Override
   public String getCommandToken() {
      return "eventlet";
   }

   @Override
   public String getCommandDescription() {
      return "Relay eventlet commands.";
   }
}
