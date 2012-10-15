package org.atomnuke.fallout.service;

/**
 *
 * @author zinic
 */
public class ServiceUnavailableException extends Exception {

   private final Class serviceClass;

   public ServiceUnavailableException(Class serviceClass, String message) {
      super(message);

      this.serviceClass = serviceClass;
   }

   public ServiceUnavailableException(Class serviceClass, String message, Throwable cause) {
      super(message, cause);

      this.serviceClass = serviceClass;
   }

   public ServiceUnavailableException(Class serviceClass, Throwable cause) {
      super(cause);

      this.serviceClass = serviceClass;
   }

   public Class serviceClass() {
      return serviceClass;
   }
}
