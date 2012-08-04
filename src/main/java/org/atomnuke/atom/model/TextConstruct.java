package org.atomnuke.atom.model;

import org.atomnuke.atom.model.annotation.ElementValue;

/**
 *
 * @author zinic
 */
public interface TextConstruct extends AtomCommonAtributes {

   Type type();

   @ElementValue
   String value();
}
