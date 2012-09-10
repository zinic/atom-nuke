package org.atomnuke.atom.io.reader.sax;

import org.atomnuke.atom.xml.AtomElement;

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
