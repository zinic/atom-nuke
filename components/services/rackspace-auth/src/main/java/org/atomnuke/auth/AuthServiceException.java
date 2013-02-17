package org.atomnuke.auth;

/**
 *
 * @author zinic
 */
public class AuthServiceException extends Exception {

   public AuthServiceException(String message) {
      super(message);
   }

   public AuthServiceException(String message, Throwable cause) {
      super(message, cause);
   }

   public AuthServiceException(Throwable cause) {
      super(cause);
   }
}
