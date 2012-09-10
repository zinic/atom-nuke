package org.atomnuke.cli.command.eps.relay;

import java.util.Iterator;
import org.atomnuke.cli.command.AbstractNukeCommand;
import org.atomnuke.config.ConfigurationHandler;
import org.atomnuke.config.ConfigurationReader;
import org.atomnuke.config.model.Relay;
import org.atomnuke.util.cli.command.result.CommandFailure;
import org.atomnuke.util.cli.command.result.CommandResult;
import org.atomnuke.util.cli.command.result.CommandSuccess;

/**
 *
 * @author zinic
 */
   public class DeleteRelay extends AbstractNukeCommand {

   private static final int RELAY_ID = 0;

   public DeleteRelay(ConfigurationReader configurationReader) {
      super(configurationReader);
   }

   @Override
   public String getCommandToken() {
      return "rm";
   }

   @Override
   public String getCommandDescription() {
      return "Removes a relay definition. This will unbind any sources or eventlets bound to the relay being deleted.";
   }

   @Override
   public CommandResult perform(String[] arguments) throws Exception {
      if (arguments.length != 1) {
         return new CommandFailure("Deleting a relay requires one argument: <relay-id>");
      }

      final ConfigurationHandler cfgHandler = getConfigurationReader().readConfiguration();

      for (Iterator<Relay> relayItr = cfgHandler.getRelays().iterator(); relayItr.hasNext();) {
         if (relayItr.next().getId().equals(arguments[RELAY_ID])) {
            relayItr.remove();
            unbindTarget(cfgHandler, arguments[RELAY_ID]);
            unbindReciever(cfgHandler, arguments[RELAY_ID]);
            
            cfgHandler.write();
            return new CommandSuccess();
         }
      }

      return new CommandFailure("No relay with an id matching, \"" + arguments[RELAY_ID] + "\" seems to exist.");
   }
}
