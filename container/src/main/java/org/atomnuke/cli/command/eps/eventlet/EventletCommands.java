package org.atomnuke.cli.command.eps.eventlet;

import org.atomnuke.cli.CliConfigurationHandler;
import org.atomnuke.config.model.Eventlet;
import org.atomnuke.util.cli.command.AbstractCommandList;
import org.atomnuke.util.cli.command.result.CommandResult;
import org.atomnuke.util.cli.command.result.MessageResult;

/**
 *
 * @author zinic
 */
public class EventletCommands extends AbstractCommandList {

   public EventletCommands(CliConfigurationHandler configurationHandler) {
      super(configurationHandler, new AddEventlet(configurationHandler), new DeleteEventlet(configurationHandler));
   }

   @Override
   public CommandResult perform() throws Exception {
      final CliConfigurationHandler cfgHandler = getConfigHandler();
      final StringBuilder output = new StringBuilder();

      for (Eventlet eventlet : cfgHandler.getEventlets()) {
         output.append("Eventlet definition, ").append(eventlet.getId()).append("\" binds ").append(eventlet.getHref()).append(" as language type ").append(eventlet.getType().name().toLowerCase()).append("\n");
      }

      return new MessageResult(output.toString());
   }

   @Override
   public String getCommandToken() {
      return "eventlets";
   }

   @Override
   public String getCommandDescription() {
      return "Eventlets and related commands.";
   }
}
