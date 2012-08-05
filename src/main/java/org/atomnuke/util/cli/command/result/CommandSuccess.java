package org.atomnuke.util.cli.command.result;

/**
 *
 * @author zinic
 */
public class CommandSuccess extends MessageResult {

   public CommandSuccess() {
      this("Ok.");
   }

   public CommandSuccess(String message) {
      super(message);
   }

   @Override
   public int getStatusCode() {
      return StatusCodes.OK.intValue();
   }
}
