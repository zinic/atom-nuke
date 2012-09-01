package org.atomnuke.atom.model;

import org.atomnuke.atom.model.annotation.ElementValue;

/**
 *
 * @author zinic
 */
public interface TypedContent extends AtomCommonAtributes {

   Type type();

   @ElementValue
   @Override
   String toString();
}
