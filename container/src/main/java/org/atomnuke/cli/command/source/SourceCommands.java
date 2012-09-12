package org.atomnuke.cli.command.source;

import org.atomnuke.config.server.ServerConfigurationHandler;
import org.atomnuke.util.cli.command.AbstractCommandList;

/**
 *
 * @author zinic
 */
public class SourceCommands extends AbstractCommandList {

   public SourceCommands(ServerConfigurationHandler configurationHandler) {
      super(new AddSource(configurationHandler), new DeleteSource(configurationHandler), new ListSources(configurationHandler));
   }

   @Override
   public String getCommandToken() {
      return "source";
   }

   @Override
   public String getCommandDescription() {
      return "Source commands.";
   }
}
