package org.atomnuke.command.sink;

import java.util.Iterator;
import org.atomnuke.cli.command.AbstractNukeCommand;
import org.atomnuke.config.ConfigurationHandler;
import org.atomnuke.config.ConfigurationReader;
import org.atomnuke.config.model.Binding;
import org.atomnuke.config.model.Sink;
import org.atomnuke.util.cli.command.result.CommandFailure;
import org.atomnuke.util.cli.command.result.CommandResult;
import org.atomnuke.util.cli.command.result.CommandSuccess;

/**
 *
 * @author zinic
 */
public class DeleteSink extends AbstractNukeCommand {

   private static final int SINK_ID = 0;

   public DeleteSink(ConfigurationReader configurationReader) {
      super(configurationReader);
   }

   @Override
   public String getCommandToken() {
      return "rm";
   }

   @Override
   public String getCommandDescription() {
      return "Removes a sink definition. This will unbind any targets bound to the sink being deleted.";
   }

   @Override
   public CommandResult perform(String[] arguments) throws Exception {
      if (arguments.length != 1) {
         return new CommandFailure("Deleting a sink requires one arguments: <sink-id>");
      }

      final ConfigurationHandler cfgHandler = getConfigurationReader().readConfiguration();

      for (Iterator<Sink> sinkItr = cfgHandler.getSinks().iterator(); sinkItr.hasNext();) {
         if (sinkItr.next().getId().equals(arguments[SINK_ID])) {
            sinkItr.remove();
            unbindReciever(cfgHandler, arguments[SINK_ID]);
            
            cfgHandler.write();
            break;
         }
      }
         return new CommandFailure("No sink with an id matching, \"" + arguments[SINK_ID] + "\" seems to exist.");
   }
}
