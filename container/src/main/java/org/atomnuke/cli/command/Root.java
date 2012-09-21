package org.atomnuke.cli.command;

import org.atomnuke.cli.command.binding.BindingCommands;
import org.atomnuke.cli.command.eps.EPSCommands;
import org.atomnuke.cli.command.server.ServerCommands;
import org.atomnuke.cli.command.source.SourceCommands;
import org.atomnuke.cli.command.sink.SinkCommands;
import org.atomnuke.cli.CliConfigurationHandler;
import org.atomnuke.cli.command.param.ParamsCommand;
import org.atomnuke.util.cli.command.RootCommand;

/**
 *
 * @author zinic
 */
public class Root extends RootCommand {

   // Aw yeah, dependency inversion...
   // This should totally be done by an IOC container or something >_>
   public Root(CliConfigurationHandler configurationHandler) {
      super(configurationHandler, new ServerCommands(configurationHandler), new EPSCommands(configurationHandler),
              new SourceCommands(configurationHandler), new SinkCommands(configurationHandler), new BindingCommands(configurationHandler),
              new ParamsCommand(configurationHandler));
   }
}
