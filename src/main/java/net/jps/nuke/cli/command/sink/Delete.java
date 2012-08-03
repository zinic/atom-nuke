package net.jps.nuke.cli.command.sink;

import java.util.Iterator;
import net.jps.nuke.cli.command.AbstractNukeCommand;
import net.jps.nuke.config.ConfigurationHandler;
import net.jps.nuke.config.ConfigurationReader;
import net.jps.nuke.config.model.Sink;
import net.jps.nuke.util.cli.command.result.CommandFailure;
import net.jps.nuke.util.cli.command.result.CommandResult;
import net.jps.nuke.util.cli.command.result.CommandSuccess;

/**
 *
 * @author zinic
 */
   public class Delete extends AbstractNukeCommand {

   private static final int SINK_ID = 0;

   public Delete(ConfigurationReader configurationReader) {
      super(configurationReader);
   }

   @Override
   public String getCommandToken() {
      return "rm";
   }

   @Override
   public String getCommandDescription() {
      return "Removes a sink definition. This will unbind any sources tied to the sink being deleted.";
   }

   @Override
   public CommandResult perform(String[] arguments) throws Exception {
      if (arguments.length != 1) {
         return new CommandFailure("Adding a sink requires one arguments: <sink-id>");
      }

      final ConfigurationHandler cfgHandler = getConfigurationReader().readConfiguration();

      for (Iterator<Sink> sinkItr = getSinks(cfgHandler).iterator(); sinkItr.hasNext();) {
         if (sinkItr.next().getId().equals(arguments[SINK_ID])) {
            sinkItr.remove();
            cfgHandler.write();
            
            return new CommandSuccess();
         }
      }

      return new CommandFailure("No sink with an id matching, \"" + arguments[SINK_ID] + "\" seems to exist.");
   }
}
