package org.atomnuke.util.cli.command;

import org.atomnuke.cli.CliConfigurationHandler;
import org.atomnuke.cli.command.AbstractNukeCommand;
import org.atomnuke.util.cli.command.result.CommandResult;
import org.atomnuke.util.cli.command.result.MessageResult;

/**
 *
 * @author zinic
 */
public abstract class AbstractCommandList extends AbstractNukeCommand {

   private final Command[] availableCommands;

   public AbstractCommandList(CliConfigurationHandler cfgHandler, Command... availableCommands) {
      super(cfgHandler);

      this.availableCommands = availableCommands;
   }

   private CommandResult usage() {
      final StringBuilder message = new StringBuilder("Available commands: \r\n");

      for (Command availableCommand : availableCommands()) {
         message.append(availableCommand.getCommandToken());

         // TODO: Fix this one day
         if (availableCommand.getCommandToken().length() > 7) {
            message.append("\t");
         } else {
            message.append("\t\t");
         }

         message.append(availableCommand.getCommandDescription());
         message.append("\n");
      }

      return new MessageResult(message.toString());
   }

   @Override
   public final Command[] availableCommands() {
      return availableCommands;
   }

   public CommandResult perform() throws Exception {
      return usage();
   }

   @Override
   public CommandResult perform(String[] arguments) throws Exception {
      return arguments.length > 0 ? usage() : perform();
   }
}
