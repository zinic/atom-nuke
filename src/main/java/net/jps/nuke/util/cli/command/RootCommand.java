package net.jps.nuke.util.cli.command;

/**
 *
 * @author zinic
 */
public class RootCommand extends AbstractCommandList {

    public RootCommand(Command... availableCommands) {
        super(availableCommands);
    }

    @Override
    public String getCommandDescription() {
        throw new UnsupportedOperationException("Root command has no description.");
    }

    @Override
    public String getCommandToken() {
        throw new UnsupportedOperationException("Root command has no token.");
    }
}
