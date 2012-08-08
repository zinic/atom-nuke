package org.atomnuke.util.cli;

import java.util.Arrays;
import org.atomnuke.util.cli.command.Command;
import org.atomnuke.util.cli.command.result.CommandResult;
import org.atomnuke.util.cli.command.result.ExceptionResult;

/**
 *
 * @author zinic
 */
public class CommandDriver {

   private final Command myCommand;
   private final String[] args;

   public CommandDriver(Command command, String[] args) {
      this.myCommand = command;
      this.args = Arrays.copyOf(args, args.length);
   }

   public CommandResult go() {
      try {
         return args.length > 0 ? nextCommand(args[0]) : myCommand.perform(args);
      } catch (Exception ex) {
         return new ExceptionResult(ex);
      }
   }

   public CommandResult nextCommand(String nextArgument) throws Exception {
      if (nextArgument == null || nextArgument.length() == 0) {
         throw new IllegalArgumentException();
      }

      for (Command availableCommand : myCommand.availableCommands()) {
         if (availableCommand.getCommandToken().equalsIgnoreCase(nextArgument)) {
            return new CommandDriver(availableCommand, Arrays.copyOfRange(args, 1, args.length)).go();
         }
      }

      return myCommand.perform(args);
   }
}
