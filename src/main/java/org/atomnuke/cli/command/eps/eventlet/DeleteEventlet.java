package org.atomnuke.cli.command.eps.eventlet;

import java.util.Iterator;
import org.atomnuke.cli.command.AbstractNukeCommand;
import org.atomnuke.config.ConfigurationHandler;
import org.atomnuke.config.ConfigurationReader;
import org.atomnuke.config.model.Eventlet;
import org.atomnuke.util.cli.command.result.CommandFailure;
import org.atomnuke.util.cli.command.result.CommandResult;
import org.atomnuke.util.cli.command.result.CommandSuccess;

/**
 *
 * @author zinic
 */
public class DeleteEventlet extends AbstractNukeCommand {

   private static final int EVENTLET_ID = 0;

   public DeleteEventlet(ConfigurationReader configurationReader) {
      super(configurationReader);
   }

   @Override
   public String getCommandToken() {
      return "rm";
   }

   @Override
   public String getCommandDescription() {
      return "Removes an eventlet definition. This will unbind the eventlet from all relays its registered to.";
   }

   @Override
   public CommandResult perform(String[] arguments) throws Exception {
      if (arguments.length != 1) {
         return new CommandFailure("Deleting a source requires one arguments: <sink-id>");
      }

      final ConfigurationHandler cfgHandler = getConfigurationReader().readConfiguration();

      for (Iterator<Eventlet> eventletItr = getEventlets(cfgHandler).iterator(); eventletItr.hasNext();) {
         if (eventletItr.next().getId().equals(arguments[EVENTLET_ID])) {
            eventletItr.remove();
            cfgHandler.write();

            return new CommandSuccess();
         }
      }

      return new CommandFailure("No eventlet with an id matching, \"" + arguments[EVENTLET_ID] + "\" seems to exist.");
   }
}
