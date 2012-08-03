package net.jps.nuke.cli.command.source;

import net.jps.nuke.config.ConfigurationReader;
import net.jps.nuke.util.cli.command.AbstractCommandList;

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
