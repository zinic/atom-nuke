package org.atomnuke.atom.model;

import org.atomnuke.atom.model.annotation.ComplexConstraint;
import org.atomnuke.atom.model.annotation.Required;
import org.atomnuke.atom.model.constraint.AtomUriConstraint;
import org.atomnuke.atom.model.constraint.MediaTypeConstraint;
import org.atomnuke.atom.model.constraint.XmlLangConstraint;

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
