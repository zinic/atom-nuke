package net.jps.nuke.atom.model;

import net.jps.nuke.atom.model.annotation.ComplexConstraint;
import net.jps.nuke.atom.model.annotation.Required;
import net.jps.nuke.atom.model.constraint.AtomUriConstraint;
import net.jps.nuke.atom.model.constraint.EmailConstraint;

/**
 *
 * @author zinic
 */
public interface PersonConstruct extends AtomCommonAtributes {

   @Required
   String name();

   @ComplexConstraint(EmailConstraint.class)
   String email();

   @ComplexConstraint(AtomUriConstraint.class)
   String uri();
}
