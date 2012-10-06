package org.atomnuke.container.packaging;

/**
 *
 * @author zinic
 */
public class UnpackerException extends Exception {

   public UnpackerException(String message) {
      super(message);
   }

   public UnpackerException(String message, Throwable cause) {
      super(message, cause);
   }

   public UnpackerException(Throwable cause) {
      super(cause);
   }
}
