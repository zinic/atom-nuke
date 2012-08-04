package org.atomnuke.atom.model;

import org.atomnuke.atom.model.annotation.ComplexConstraint;
import org.atomnuke.atom.model.annotation.ElementValue;
import org.atomnuke.atom.model.constraint.AtomUriConstraint;

/**
 *
 * @author zinic
 */
public interface Icon extends AtomCommonAtributes {

   @ElementValue
   @ComplexConstraint(AtomUriConstraint.class)
   String value();
}
