package net.jps.nuke.service;

/**
 *
 * @author zinic
 */
public class ServiceInitializationException extends Exception {

   public ServiceInitializationException(String message) {
      super(message);
   }

   public ServiceInitializationException(Throwable cause) {
      super(cause);
   }

   public ServiceInitializationException(String message, Throwable cause) {
      super(message, cause);
   }
}
