package org.atomnuke.cli.command.source;

import org.atomnuke.cli.CliConfigurationHandler;
import org.atomnuke.config.model.Source;
import org.atomnuke.util.cli.command.AbstractCommandList;
import org.atomnuke.util.cli.command.result.CommandResult;
import org.atomnuke.util.cli.command.result.MessageResult;

/**
 *
 * @author zinic
 */
public class SourceCommands extends AbstractCommandList {

   public SourceCommands(CliConfigurationHandler configurationHandler) {
      super(configurationHandler, new AddSource(configurationHandler), new DeleteSource(configurationHandler));
   }

   @Override
   public CommandResult perform() throws Exception {
      final CliConfigurationHandler cfgHandler = getConfigHandler();
      final StringBuilder output = new StringBuilder();

      for (Source source : cfgHandler.getSources()) {
         output.append("Source definition, ").append(source.getId()).append(" binds ").append(source.getHref()).append(" as language type ").append(source.getType().name().toLowerCase()).append("\n");
      }

      return new MessageResult(output.toString());
   }

   @Override
   public String getCommandToken() {
      return "sources";
   }

   @Override
   public String getCommandDescription() {
      return "Source commands.";
   }
}
