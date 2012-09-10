package org.atomnuke.atom.io;

/**
 *
 * @author zinic
 */
public class AtomReadException extends Exception {

   public AtomReadException(Throwable cause) {
      super(cause);
   }

   public AtomReadException(String message, Throwable cause) {
      super(message, cause);
   }

   public AtomReadException(String message) {
      super(message);
   }
}
