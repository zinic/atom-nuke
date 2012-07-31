package net.jps.nuke.listener.eps.handler;

/**
 *
 * @author zinic
 */
public class EventProcessingException extends Exception {

   public EventProcessingException(String message) {
      super(message);
   }

   public EventProcessingException(String message, Throwable cause) {
      super(message, cause);
   }
}
