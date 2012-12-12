package org.atomnuke.container.packaging;

/**
 *
 * @author zinic
 */
public class MissingDigestImplementationException extends RuntimeException {

   public MissingDigestImplementationException(String message) {
      super(message);
   }

   public MissingDigestImplementationException(String message, Throwable cause) {
      super(message, cause);
   }

   public MissingDigestImplementationException(Throwable cause) {
      super(cause);
   }
}
