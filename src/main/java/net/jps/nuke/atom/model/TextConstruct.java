package net.jps.nuke.atom.model;

import net.jps.nuke.atom.model.annotation.ElementValue;

/**
 *
 * @author zinic
 */
public interface TextConstruct extends AtomCommonAtributes {

   Type type();

   @ElementValue
   String value();
}
