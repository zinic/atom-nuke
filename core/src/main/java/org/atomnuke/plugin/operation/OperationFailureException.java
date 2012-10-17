package org.atomnuke.plugin.operation;

/**
 *
 * @author zinic
 */
public class OperationFailureException extends Exception {

   public OperationFailureException(String message) {
      super(message);
   }

   public OperationFailureException(String message, Throwable cause) {
      super(message, cause);
   }

   public OperationFailureException(Throwable cause) {
      super(cause);
   }
}
