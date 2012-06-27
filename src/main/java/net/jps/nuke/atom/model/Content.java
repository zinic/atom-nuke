package net.jps.nuke.atom.model;

import net.jps.nuke.atom.model.annotation.ComplexConstraint;
import net.jps.nuke.atom.model.constraint.AtomUriConstraint;

/**
 *
 * @author zinic
 */
public interface Content {

   String type();

   @ComplexConstraint(AtomUriConstraint.class)
   String src();
}
