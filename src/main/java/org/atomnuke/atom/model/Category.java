package org.atomnuke.atom.model;

import org.atomnuke.atom.model.annotation.ComplexConstraint;
import org.atomnuke.atom.model.annotation.Required;
import org.atomnuke.atom.model.constraint.AtomUriConstraint;

/**
 *
 * @author zinic
 */
public interface Category extends AtomCommonAtributes {

   @Required
   String term();

   @ComplexConstraint(AtomUriConstraint.class)
   String scheme();

   String label();
}
