package org.atomnuke.cli.command.eps.eventlet;

import org.atomnuke.config.ConfigurationReader;
import org.atomnuke.util.cli.command.AbstractCommandList;

/**
 *
 * @author zinic
 */
public class EventletCommands extends AbstractCommandList {

   public EventletCommands(ConfigurationReader configurationReader) {
      super(new AddEventlet(configurationReader), new DeleteEventlet(configurationReader), new ListEventlets(configurationReader));
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
