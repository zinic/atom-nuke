package org.atomnuke.util.cli.command.result;

/**
 *
 * @author zinic
 */
public interface CommandResult {

   boolean shouldExit();

   int getStatusCode();

   String getStringResult();
}
