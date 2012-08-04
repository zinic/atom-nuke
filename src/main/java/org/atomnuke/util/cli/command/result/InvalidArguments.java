package org.atomnuke.util.cli.command.result;

/**
 *
 * @author zinic
 */
public class InvalidArguments extends MessageResult {

   public InvalidArguments(String message) {
      super(message);
   }

   @Override
   public int getStatusCode() {
      return StatusCodes.INVALID_ARGUMENTS.intValue();
   }
}
