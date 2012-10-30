package org.atomnuke.sink;

/**
 *
 * @author zinic
 */
public class AtomSinkException extends Exception {

   public AtomSinkException(String message) {
      super(message);
   }

   public AtomSinkException(String message, Throwable cause) {
      super(message, cause);
   }

   public AtomSinkException(Throwable cause) {
      super(cause);
   }
}
