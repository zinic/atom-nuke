package org.atomnuke.atom.model;

import org.atomnuke.atom.model.annotation.ComplexConstraint;
import org.atomnuke.atom.model.annotation.Required;
import org.atomnuke.atom.model.constraint.AtomUriConstraint;
import org.atomnuke.atom.model.constraint.EmailConstraint;

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
