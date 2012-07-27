package net.jps.nuke.listener;

/**
 *
 * @author zinic
 */
public class AtomListenerException extends Exception {

   public AtomListenerException(String message) {
      super(message);
   }

   public AtomListenerException(String message, Throwable cause) {
      super(message, cause);
   }

   public AtomListenerException(Throwable cause) {
      super(cause);
   }
}
