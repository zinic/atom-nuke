package net.jps.nuke.atom.sax;

import net.jps.nuke.atom.stax.AtomElement;

/**
 *
 * @author zinic
 */
public class InvalidElementException extends IllegalStateException {

   private AtomElement invalidElement;

   public InvalidElementException(AtomElement invalidElement, String s) {
      super(s);
      this.invalidElement = invalidElement;
   }

   public AtomElement getInvalidElement() {
      return invalidElement;
   }
}
