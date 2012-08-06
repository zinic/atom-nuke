package org.atomnuke.atom.model;

import org.atomnuke.atom.model.annotation.ComplexConstraint;
import org.atomnuke.atom.model.annotation.ElementValue;
import org.atomnuke.atom.model.constraint.AtomUriConstraint;

/**
 *
 * @author zinic
 */
public interface Generator extends AtomCommonAtributes {

   @ComplexConstraint(AtomUriConstraint.class)
   String uri();

   String version();

   @ElementValue
   @Override
   String toString();
}
