package org.atomnuke.command.sink;

import org.atomnuke.cli.command.AbstractNukeCommand;
import org.atomnuke.config.ConfigurationHandler;
import org.atomnuke.config.ConfigurationReader;
import org.atomnuke.config.model.Sink;
import org.atomnuke.util.cli.command.result.CommandResult;
import org.atomnuke.util.cli.command.result.MessageResult;

/**
 *
 * @author zinic
 */
public class ListSinks extends AbstractNukeCommand {

   public ListSinks(ConfigurationReader configurationReader) {
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

      for (Sink sink : getSinks(cfgHandler)) {
         output.append("Sink definition, \"").append(sink.getId()).append("\" binds ").append(sink.getHref()).append(" as language type ").append(sink.getType().name().toLowerCase()).append("\n");
      }

      return new MessageResult(output.toString());
   }
}
