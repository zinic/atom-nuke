package org.atomnuke.cli.command.source;

import org.atomnuke.config.ConfigurationReader;
import org.atomnuke.util.cli.command.AbstractCommandList;

/**
 *
 * @author zinic
 */
public class SourceCommands extends AbstractCommandList {

   public SourceCommands(ConfigurationReader configurationReader) {
      super(new Add(configurationReader), new List(configurationReader));
   }

   @Override
   public String getCommandToken() {
      return "sink";
   }

   @Override
   public String getCommandDescription() {
      return "Sink commands.";
   }
}
