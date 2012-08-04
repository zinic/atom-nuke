package org.atomnuke.listener.eps.eventlet;

/**
 *
 * @author zinic
 */
public class AtomEventletException extends Exception {

   public AtomEventletException(String message) {
      super(message);
   }

   public AtomEventletException(String message, Throwable cause) {
      super(message, cause);
   }
}
