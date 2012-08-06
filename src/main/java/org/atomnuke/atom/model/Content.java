package org.atomnuke.atom.model;

import org.atomnuke.atom.model.annotation.ComplexConstraint;
import org.atomnuke.atom.model.annotation.ElementValue;
import org.atomnuke.atom.model.constraint.AtomUriConstraint;

/**
 *
 * @author zinic
 */
public interface Content extends AtomCommonAtributes {

   String type();

   @ComplexConstraint(AtomUriConstraint.class)
   String src();

   @ElementValue
   @Override
   String toString();
}
