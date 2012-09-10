package org.atomnuke.atom;


/**
 * @deprecated org.atomnuke.atom.io replaces this package
 *
 * @author zinic
 */

@Deprecated
public class AtomParserException extends Exception {

   public AtomParserException(String message, Throwable cause) {
      super(message, cause);
   }

   public AtomParserException(String message) {
      super(message);
   }
}
