package org.atomnuke.task.lifecycle;

/**
 *
 * @author zinic
 */
public class DestructionException extends RuntimeException {

   public DestructionException(String message) {
      super(message);
   }

   public DestructionException(Throwable cause) {
      super(cause);
   }

   public DestructionException(String message, Throwable cause) {
      super(message, cause);
   }
}
