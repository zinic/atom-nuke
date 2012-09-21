package org.atomnuke.util.cli.command;

import org.atomnuke.cli.CliConfigurationHandler;

/**
 *
 * @author zinic
 */
public class RootCommand extends AbstractCommandList {

    public RootCommand(CliConfigurationHandler handler, Command... availableCommands) {
        super(handler, availableCommands);
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
