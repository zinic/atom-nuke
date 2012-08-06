package org.atomnuke.util.cli.command;

import org.atomnuke.util.cli.command.result.CommandResult;

/**
 *
 * @author zinic
 */
public interface Command {

   String getCommandToken();
   
   String getCommandDescription();
   
   CommandResult perform(String[] arguments) throws Exception;
   
   Command[] availableCommands();
}
