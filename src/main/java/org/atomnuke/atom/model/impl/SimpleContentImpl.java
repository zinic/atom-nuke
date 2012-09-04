package org.atomnuke.atom.model.impl;

import org.atomnuke.atom.model.SimpleContent;

/**
 *
 * @author zinic
 */
public class SimpleContentImpl extends AtomCommonAttributesImpl implements SimpleContent {

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

   @Override
   public boolean equals(Object obj) {
      if (obj == null || getClass() != obj.getClass()) {
         return false;
      }

      final SimpleContentImpl other = (SimpleContentImpl) obj;

      if ((this.value == null) ? (other.value != null) : !this.value.equals(other.value)) {
         return false;
      }

      return super.equals(obj);
   }
}
