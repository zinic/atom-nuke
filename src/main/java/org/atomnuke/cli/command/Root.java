package org.atomnuke.cli.command;

import org.atomnuke.cli.command.server.ServerCommands;
import org.atomnuke.cli.command.source.SourceCommands;
import org.atomnuke.command.sink.SinkCommands;
import org.atomnuke.config.ConfigurationReader;
import org.atomnuke.util.cli.command.RootCommand;

/**
 *
 * @author zinic
 */
public class Root extends RootCommand {

   public Root(ConfigurationReader configurationReader) {
      super(new ServerCommands(configurationReader), new SourceCommands(configurationReader), new SinkCommands(configurationReader));
   }
}
