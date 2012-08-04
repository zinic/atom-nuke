package org.atomnuke.atom.model.constraint;

/**
 *
 * @author zinic
 */
public class AtomUriConstraint implements ValueConstraint {

   public String name() {
      return "atomUri";
   }

   public String pattern() {
      return ".*";
   }
}
