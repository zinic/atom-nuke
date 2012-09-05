package org.atomnuke.atom.model.constraint;

/**
 *
 * @author zinic
 */
public class EmailConstraint implements ValueConstraint {

   public String name() {
      return "atomEmailAddress";
   }

   public String pattern() {
      throw new UnsupportedOperationException("Not supported yet.");
   }
}
