package org.atomnuke.cli.command.eps.eventlet;

import org.atomnuke.cli.command.AbstractNukeCommand;
import org.atomnuke.config.ConfigurationHandler;
import org.atomnuke.config.ConfigurationReader;
import org.atomnuke.config.model.Eventlet;
import org.atomnuke.util.cli.command.result.CommandResult;
import org.atomnuke.util.cli.command.result.MessageResult;

/**
 *
 * @author zinic
 */
public class ListEventlets extends AbstractNukeCommand {

   public ListEventlets(ConfigurationReader configurationReader) {
      super(configurationReader);
   }

   @Override
   public String getCommandToken() {
      return "list";
   }

   @Override
   public String getCommandDescription() {
      return "Lists all registered sinks.";
   }

   @Override
   public CommandResult perform(String[] arguments) throws Exception {
      final ConfigurationHandler cfgHandler = getConfigurationReader().readConfiguration();
      final StringBuilder output = new StringBuilder();

      for (Eventlet eventlet : getEventlets(cfgHandler)) {
         output.append("Eventlet definition, \"").append(eventlet.getId()).append("\" binds ").append(eventlet.getHref()).append(" as language type ").append(eventlet.getType().name().toLowerCase()).append("\n");
      }

      return new MessageResult(output.toString());
   }
}
