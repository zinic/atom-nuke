package net.jps.nuke.atom.model;

import net.jps.nuke.atom.model.annotation.ComplexConstraint;
import net.jps.nuke.atom.model.annotation.Required;
import net.jps.nuke.atom.model.constraint.AtomUriConstraint;

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
