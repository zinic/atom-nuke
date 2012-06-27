package net.jps.nuke.atom.model;

import net.jps.nuke.atom.model.annotation.ComplexConstraint;
import net.jps.nuke.atom.model.annotation.Required;
import net.jps.nuke.atom.model.constraint.AtomUriConstraint;
import net.jps.nuke.atom.model.constraint.MediaTypeConstraint;
import net.jps.nuke.atom.model.constraint.XmlLangConstraint;

/**
 *
 * @author zinic
 */
public interface Link extends AtomCommonAtributes {

   @Required
   @ComplexConstraint(AtomUriConstraint.class)
   String href();

   @ComplexConstraint(AtomUriConstraint.class)
   String rel();

   @ComplexConstraint(MediaTypeConstraint.class)
   String type();

   @ComplexConstraint(XmlLangConstraint.class)
   String hreflang();

   String title();

   Integer length();
}
