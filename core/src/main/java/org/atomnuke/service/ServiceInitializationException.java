package org.atomnuke.service;

/**
 *
 * @author zinic
 */
public class ServiceInitializationException extends Exception {

   public ServiceInitializationException(String message) {
      super(message);
   }

   public ServiceInitializationException(String message, Throwable cause) {
      super(message, cause);
   }

   public ServiceInitializationException(Throwable cause) {
      super(cause);
   }
}
