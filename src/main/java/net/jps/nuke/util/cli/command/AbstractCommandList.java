package net.jps.nuke.util.cli.command;

import net.jps.nuke.util.cli.command.result.CommandResult;
import net.jps.nuke.util.cli.command.result.MessageResult;

/**
 *
 * @author zinic
 */
public abstract class AbstractCommandList extends AbstractCommand {

    private final Command[] availableCommands;

    public AbstractCommandList(Command... availableCommands) {
        this.availableCommands = availableCommands;
    }

    @Override
    public final Command[] availableCommands() {
        return availableCommands;
    }

    @Override
    public CommandResult perform(String[] arguments) {
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
}
