package net.jps.nuke.util.cli.command.result;

/**
 *
 * @author zinic
 */
public enum StatusCodes {

   OK(0),
   INVALID_ARGUMENTS(1),
   NOTHING_TO_DO(2),
   SYSTEM_PRECONDITION_FAILURE(3);
   
   //
   private final int statusCode;

   private StatusCodes(int statusCode) {
      this.statusCode = statusCode;
   }

   public int intValue() {
      return statusCode;
   }
}
