package org.atomnuke.cli.command.eps.relay;

import org.atomnuke.cli.command.source.*;
import java.util.Iterator;
import org.atomnuke.cli.command.AbstractNukeCommand;
import org.atomnuke.config.ConfigurationHandler;
import org.atomnuke.config.ConfigurationReader;
import org.atomnuke.config.model.Source;
import org.atomnuke.util.cli.command.result.CommandFailure;
import org.atomnuke.util.cli.command.result.CommandResult;
import org.atomnuke.util.cli.command.result.CommandSuccess;

/**
 *
 * @author zinic
 */
   public class DeleteRelay extends AbstractNukeCommand {

   private static final int SINK_ID = 0;

   public DeleteRelay(ConfigurationReader configurationReader) {
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
         return new CommandFailure("Deleting a source requires one arguments: <sink-id>");
      }

      final ConfigurationHandler cfgHandler = getConfigurationReader().readConfiguration();

      for (Iterator<Source> sourceItr = getSources(cfgHandler).iterator(); sourceItr.hasNext();) {
         if (sourceItr.next().getId().equals(arguments[SINK_ID])) {
            sourceItr.remove();
            cfgHandler.write();
            
            return new CommandSuccess();
         }
      }

      return new CommandFailure("No source with an id matching, \"" + arguments[SINK_ID] + "\" seems to exist.");
   }
}
