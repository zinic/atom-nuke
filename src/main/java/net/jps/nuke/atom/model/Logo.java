package net.jps.nuke.atom.model;

import net.jps.nuke.atom.model.annotation.ComplexConstraint;
import net.jps.nuke.atom.model.annotation.ElementValue;
import net.jps.nuke.atom.model.constraint.AtomUriConstraint;

/**
 *
 * @author zinic
 */
public interface Logo extends AtomCommonAtributes {

   @ElementValue
   @ComplexConstraint(AtomUriConstraint.class)
   String value();
}
