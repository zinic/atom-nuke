package net.jps.nuke.atom;

/**
 *
 * @author zinic
 */
public class AtomParserException extends Exception {

   public AtomParserException(String message, Throwable cause) {
      super(message, cause);
   }

   public AtomParserException(String message) {
      super(message);
   }
}
