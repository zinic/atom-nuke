package org.atomnuke.atom.model;

import org.atomnuke.atom.model.annotation.ElementValue;

/**
 * The SimpleContent interface his is a non-Atom model element designed to
 * simply this particular implementation.
 *
 * @author zinic
 */
public interface SimpleContent extends AtomCommonAtributes {

   /**
    * The toString method on a model object that extends this interface will
    * return the Java string representing this element's simple content.
    *
    * @return
    */
   @ElementValue
   @Override
   String toString();
}
