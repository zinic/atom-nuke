package org.atomnuke.atom.io;

/**
 *
 * @author zinic
 */
public class AtomWriteException extends Exception {

   public AtomWriteException(Throwable cause) {
      super(cause);
   }

   public AtomWriteException(String message, Throwable cause) {
      super(message, cause);
   }

   public AtomWriteException(String message) {
      super(message);
   }
}
