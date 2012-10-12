package org.atomnuke.service;

/**
 *
 * @author zinic
 */
public class ServiceAlreadyRegisteredException extends Exception {

   public ServiceAlreadyRegisteredException(String message) {
      super(message);
   }

   public ServiceAlreadyRegisteredException(String message, Throwable cause) {
      super(message, cause);
   }

   public ServiceAlreadyRegisteredException(Throwable cause) {
      super(cause);
   }
}
