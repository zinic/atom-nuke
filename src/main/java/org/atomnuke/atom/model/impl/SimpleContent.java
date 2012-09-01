package org.atomnuke.atom.model.impl;

/**
 *
 * @author zinic
 */
public class SimpleContent extends AtomCommonAttributesImpl {

   private String value;

   public void setValue(String value) {
      this.value = value;
   }

   @Override
   public String toString() {
      return value.toString();
   }
}
