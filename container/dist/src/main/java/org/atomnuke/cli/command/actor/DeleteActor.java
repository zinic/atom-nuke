package org.atomnuke.cli.command.actor;

import java.util.Iterator;
import org.atomnuke.atombus.config.model.MessageActor;
import org.atomnuke.cli.CliConfigurationHandler;
import org.atomnuke.cli.command.AbstractNukeCommand;
import org.atomnuke.util.cli.command.result.CommandFailure;
import org.atomnuke.util.cli.command.result.CommandResult;
import org.atomnuke.util.cli.command.result.CommandSuccess;

/**
 *
 * @author zinic
 */
public class DeleteActor extends AbstractNukeCommand {

   private static final int ACTOR_ID = 0;

   public DeleteActor(CliConfigurationHandler configurationHandler) {
      super(configurationHandler);
   }

   @Override
   public String getCommandToken() {
      return "rm";
   }

   @Override
   public String getCommandDescription() {
      return "Removes a message actor's definition. This will unbind any recievers tied to the message actor being deleted.";
   }

   @Override
   public CommandResult perform(String[] arguments) throws Exception {
      if (arguments.length != 1) {
         return new CommandFailure("Deleting a source requires one arguments: <sink-id>");
      }

      final CliConfigurationHandler cfgHandler = getConfigHandler();

      for (Iterator<MessageActor> actorItr = cfgHandler.getMessageActors().iterator(); actorItr.hasNext();) {
         if (actorItr.next().getId().equals(arguments[ACTOR_ID])) {
            actorItr.remove();
            unbindSource(cfgHandler, arguments[ACTOR_ID]);

            cfgHandler.write();
            return new CommandSuccess();
         }
      }

      return new CommandFailure("No message actor with an id matching, \"" + arguments[ACTOR_ID] + "\" seems to exist.");
   }
}
