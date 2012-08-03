package net.jps.nuke.cli.command;

import net.jps.nuke.cli.command.server.ServerCommands;
import net.jps.nuke.cli.command.sink.SinkCommands;
import net.jps.nuke.config.ConfigurationReader;
import net.jps.nuke.util.cli.command.RootCommand;

/**
 *
 * @author zinic
 */
public class Root extends RootCommand {

   public Root(ConfigurationReader configurationReader) {
      super(new ServerCommands(configurationReader), new SinkCommands(configurationReader));
   }
}
