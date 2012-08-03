package net.jps.nuke.cli.command.source;

import net.jps.nuke.cli.command.sink.*;
import net.jps.nuke.cli.command.AbstractNukeCommand;
import net.jps.nuke.config.ConfigurationHandler;
import net.jps.nuke.config.ConfigurationReader;
import net.jps.nuke.config.model.Sink;
import net.jps.nuke.util.cli.command.result.CommandResult;
import net.jps.nuke.util.cli.command.result.MessageResult;

/**
 *
 * @author zinic
 */
public class List extends AbstractNukeCommand {

   public List(ConfigurationReader configurationReader) {
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

      if (cfgHandler.getConfiguration() != null && cfgHandler.getConfiguration().getSinks() != null) {
         for (Sink sink : cfgHandler.getConfiguration().getSinks().getSink()) {
            output.append("Sink: ").append(sink.getId()).append(" binds ").append(sink.getHref()).append(" as language type ").append(sink.getType().name()).append("\n");
         }
      }
      
      return new MessageResult(output.toString());
   }
}
