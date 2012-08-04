package org.atomnuke.source;

/**
 *
 * @author zinic
 */
public class AtomSourceException extends Exception {

   public AtomSourceException(String message) {
      super(message);
   }

   public AtomSourceException(String message, Throwable cause) {
      super(message, cause);
   }

   public AtomSourceException(Throwable cause) {
      super(cause);
   }
}
