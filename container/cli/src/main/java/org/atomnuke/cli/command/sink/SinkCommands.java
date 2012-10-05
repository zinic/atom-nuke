package org.atomnuke.cli.command.sink;

import org.atomnuke.cli.CliConfigurationHandler;
import org.atomnuke.config.model.Sink;
import org.atomnuke.util.cli.command.AbstractCommandList;
import org.atomnuke.util.cli.command.result.CommandResult;
import org.atomnuke.util.cli.command.result.MessageResult;

/**
 *
 * @author zinic
 */
public class SinkCommands extends AbstractCommandList {

   public SinkCommands(CliConfigurationHandler configurationHandler) {
      super(configurationHandler, new AddSink(configurationHandler), new DeleteSink(configurationHandler));
   }

   @Override
   public CommandResult perform() throws Exception {
      final CliConfigurationHandler cfgHandler = getConfigHandler();
      final StringBuilder output = new StringBuilder();

      for (Sink sink : cfgHandler.getSinks()) {
         output.append("Sink definition, ").append(sink.getId()).append(" binds ").append(sink.getHref()).append(" as language type ").append(sink.getType().name().toLowerCase()).append("\n");
      }

      return new MessageResult(output.toString());
   }

   @Override
   public String getCommandToken() {
      return "sinks";
   }

   @Override
   public String getCommandDescription() {
      return "Sink commands.";
   }
}
