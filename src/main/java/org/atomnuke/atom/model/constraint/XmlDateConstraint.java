package org.atomnuke.atom.model.constraint;

/**
 *
 * @author zinic
 */
public class XmlDateConstraint implements ValueConstraint {

   @Override
   public String name() {
      return "xmlDate";
   }

   @Override
   public String pattern() {
      return ".*";
   }
}
