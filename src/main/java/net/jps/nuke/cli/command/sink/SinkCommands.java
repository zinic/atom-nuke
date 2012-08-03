package net.jps.nuke.cli.command.sink;

import net.jps.nuke.config.ConfigurationReader;
import net.jps.nuke.util.cli.command.AbstractCommandList;

/**
 *
 * @author zinic
 */
public class SinkCommands extends AbstractCommandList {

   public SinkCommands(ConfigurationReader configurationReader) {
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
