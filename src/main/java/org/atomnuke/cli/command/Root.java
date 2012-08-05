package org.atomnuke.cli.command;

import org.atomnuke.cli.command.binding.BindingCommands;
import org.atomnuke.cli.command.eps.EPSCommands;
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

   // Aw yeah, dependency inversion...
   // This should totally be done by an IOC container or something >_>
   public Root(ConfigurationReader configurationReader) {
      super(new ServerCommands(configurationReader), new EPSCommands(configurationReader),
              new SourceCommands(configurationReader), new SinkCommands(configurationReader), new BindingCommands(configurationReader));
   }
}
