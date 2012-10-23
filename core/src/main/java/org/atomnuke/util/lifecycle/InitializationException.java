package org.atomnuke.util.lifecycle;

/**
 *
 * @author zinic
 */
public class InitializationException extends Exception {

   public InitializationException(String message) {
      super(message);
   }

   public InitializationException(Throwable cause) {
      super(cause);
   }

   public InitializationException(String message, Throwable cause) {
      super(message, cause);
   }
}
