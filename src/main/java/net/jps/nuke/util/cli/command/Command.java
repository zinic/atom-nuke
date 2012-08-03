package net.jps.nuke.util.cli.command;

import net.jps.nuke.util.cli.command.result.CommandResult;

/**
 *
 * @author zinic
 */
public interface Command {

   String getCommandToken();
   
   String getCommandDescription();
   
//   CommandResult perform() throws Exception;
   
   CommandResult perform(String[] arguments) throws Exception;
   
   Command[] availableCommands();
}
