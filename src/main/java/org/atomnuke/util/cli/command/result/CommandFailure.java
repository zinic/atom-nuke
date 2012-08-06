package org.atomnuke.util.cli.command.result;

/**
 *
 * @author zinic
 */
public class CommandFailure implements CommandResult {

   private final String message;
   private final int statusCode;

   public CommandFailure(String message) {
      this(1, message);
   }

   public CommandFailure(int statusCode, String message) {
      this.statusCode = statusCode;
      this.message = message;
   }

   @Override
   public boolean shouldExit() {
      return true;
   }

   @Override
   public String getStringResult() {
      return message;
   }

   @Override
   public int getStatusCode() {
      return statusCode;
   }
}
