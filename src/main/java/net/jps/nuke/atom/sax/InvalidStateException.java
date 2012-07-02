package net.jps.nuke.atom.sax;

import net.jps.nuke.atom.xml.AtomElement;

/**
 *
 * @author zinic
 */
public class InvalidStateException extends IllegalStateException {

   private AtomElement invalidElement;

   public InvalidStateException(AtomElement invalidElement, String s) {
      super(s);
      this.invalidElement = invalidElement;
   }

   public AtomElement getInvalidElement() {
      return invalidElement;
   }
}
