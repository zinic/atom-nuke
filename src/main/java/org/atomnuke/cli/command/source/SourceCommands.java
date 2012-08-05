package org.atomnuke.cli.command.source;

import org.atomnuke.config.ConfigurationReader;
import org.atomnuke.util.cli.command.AbstractCommandList;

/**
 *
 * @author zinic
 */
public class SourceCommands extends AbstractCommandList {

   public SourceCommands(ConfigurationReader configurationReader) {
      super(new AddSource(configurationReader), new DeleteSource(configurationReader), new ListSources(configurationReader));
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
