package net.jps.nuke.service;

/**
 *
 * @author zinic
 */
public class ServiceDestructionException extends Exception {

   public ServiceDestructionException(String message) {
      super(message);
   }

   public ServiceDestructionException(Throwable cause) {
      super(cause);
   }

   public ServiceDestructionException(String message, Throwable cause) {
      super(message, cause);
   }
}
