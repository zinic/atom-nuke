package org.atomnuke.cli.command.source;

import org.atomnuke.cli.command.AbstractNukeCommand;
import org.atomnuke.config.server.ServerConfigurationHandler;
import org.atomnuke.util.config.io.ConfigurationReader;
import org.atomnuke.config.model.Source;
import org.atomnuke.util.cli.command.result.CommandResult;
import org.atomnuke.util.cli.command.result.MessageResult;

/**
 *
 * @author zinic
 */
public class ListSources extends AbstractNukeCommand {

   public ListSources(ServerConfigurationHandler configurationHandler) {
      super(configurationHandler);
   }

   @Override
   public String getCommandToken() {
      return "list";
   }

   @Override
   public String getCommandDescription() {
      return "Lists all registered sources.";
   }

   @Override
   public CommandResult perform(String[] arguments) throws Exception {
      final ServerConfigurationHandler cfgHandler = getConfigHandler();
      final StringBuilder output = new StringBuilder();

      for (Source source : cfgHandler.getSources()) {
         output.append("Source definition, ").append(source.getId()).append(" binds ").append(source.getHref()).append(" as language type ").append(source.getType().name().toLowerCase()).append("\n");
      }

      return new MessageResult(output.toString());
   }
}
