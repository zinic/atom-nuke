package org.atomnuke.command.sink;

import org.atomnuke.config.ConfigurationReader;
import org.atomnuke.util.cli.command.AbstractCommandList;

/**
 *
 * @author zinic
 */
public class SinkCommands extends AbstractCommandList {

   public SinkCommands(ConfigurationReader configurationReader) {
      super(new Add(configurationReader), new Delete(configurationReader), new List(configurationReader));
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
