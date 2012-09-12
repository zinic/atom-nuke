package org.atomnuke.cli.command.eps.eventlet;

import org.atomnuke.cli.command.AbstractNukeCommand;
import org.atomnuke.config.server.ServerConfigurationHandler;
import org.atomnuke.util.config.io.ConfigurationReader;
import org.atomnuke.config.model.Eventlet;
import org.atomnuke.util.cli.command.result.CommandResult;
import org.atomnuke.util.cli.command.result.MessageResult;

/**
 *
 * @author zinic
 */
public class ListEventlets extends AbstractNukeCommand {

   public ListEventlets(ServerConfigurationHandler configurationHandler) {
      super(configurationHandler);
   }

   @Override
   public String getCommandToken() {
      return "list";
   }

   @Override
   public String getCommandDescription() {
      return "Lists all registered relay eventlets.";
   }

   @Override
   public CommandResult perform(String[] arguments) throws Exception {
      final ServerConfigurationHandler cfgHandler = getConfigHandler();
      final StringBuilder output = new StringBuilder();

      for (Eventlet eventlet : cfgHandler.getEventlets()) {
         output.append("Eventlet definition, ").append(eventlet.getId()).append("\" binds ").append(eventlet.getHref()).append(" as language type ").append(eventlet.getType().name().toLowerCase()).append("\n");
      }

      return new MessageResult(output.toString());
   }
}
