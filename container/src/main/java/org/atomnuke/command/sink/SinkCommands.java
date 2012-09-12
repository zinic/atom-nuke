package org.atomnuke.command.sink;

import org.atomnuke.config.server.ServerConfigurationHandler;
import org.atomnuke.util.cli.command.AbstractCommandList;

/**
 *
 * @author zinic
 */
public class SinkCommands extends AbstractCommandList {

   public SinkCommands(ServerConfigurationHandler configurationHandler) {
      super(new AddSink(configurationHandler), new DeleteSink(configurationHandler), new ListSinks(configurationHandler));
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
