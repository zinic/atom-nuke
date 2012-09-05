package org.atomnuke.atom.model.constraint;

/**
 *
 * @author zinic
 */
public class XmlLangConstraint implements ValueConstraint {

   public String name() {
      return "xml:lang";
   }

   public String pattern() {
      throw new UnsupportedOperationException("Not supported yet.");
   }
}
