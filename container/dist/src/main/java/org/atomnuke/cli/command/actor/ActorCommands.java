package org.atomnuke.cli.command.actor;

import org.atomnuke.cli.CliConfigurationHandler;
import org.atomnuke.config.model.MessageActor;
import org.atomnuke.util.cli.command.AbstractCommandList;
import org.atomnuke.util.cli.command.result.CommandResult;
import org.atomnuke.util.cli.command.result.MessageResult;

/**
 *
 * @author zinic
 */
public class ActorCommands extends AbstractCommandList {

   public ActorCommands(CliConfigurationHandler configurationHandler) {
      super(configurationHandler, new AddActor(configurationHandler), new DeleteActor(configurationHandler));
   }

   @Override
   public CommandResult perform() throws Exception {
      final CliConfigurationHandler cfgHandler = getConfigHandler();
      final StringBuilder output = new StringBuilder();

      for (MessageActor actor : cfgHandler.getMessageActors()) {
         output.append("Message actor \"").append(actor.getId()).append("\" binds \"").append(actor.getHref()).append("\" as a plugin with a language type of: ").append(actor.getType()).append("\n");
      }

      return new MessageResult(output.toString());
   }

   @Override
   public String getCommandToken() {
      return "actors";
   }

   @Override
   public String getCommandDescription() {
      return "Message actor commands.";
   }
}
