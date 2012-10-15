package org.atomnuke.fallout;

/**
 *
 * @author zinic
 */
public class FalloutInitException extends RuntimeException {

   public FalloutInitException(String message) {
      super(message);
   }

   public FalloutInitException(String message, Throwable cause) {
      super(message, cause);
   }

   public FalloutInitException(Throwable cause) {
      super(cause);
   }
}
