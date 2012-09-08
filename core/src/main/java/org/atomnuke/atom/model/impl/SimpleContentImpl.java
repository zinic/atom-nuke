package org.atomnuke.atom.model.impl;

import org.atomnuke.atom.model.SimpleContent;

/**
 *
 * @author zinic
 */
public abstract class SimpleContentImpl extends AtomCommonAttributesImpl implements SimpleContent {

   private String value;

   public void setValue(String value) {
      this.value = value;
   }

   @Override
   public String toString() {
      return value;
   }

   @Override
   public int hashCode() {
      int hash = 7;

      hash = 79 * hash + (this.value != null ? this.value.hashCode() : 0);

      return hash + super.hashCode();
   }
}
