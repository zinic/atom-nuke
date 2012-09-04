package org.atomnuke.atom.model.impl;

import org.atomnuke.atom.model.Category;

/**
 *
 * @author zinic
 */
public class CategoryImpl extends AtomCommonAttributesImpl implements Category {

   private String term, scheme, label;

   public void setTerm(String term) {
      this.term = term;
   }

   public void setScheme(String scheme) {
      this.scheme = scheme;
   }

   public void setLabel(String label) {
      this.label = label;
   }

   @Override
   public String term() {
      return term;
   }

   @Override
   public String scheme() {
      return scheme;
   }

   @Override
   public String label() {
      return label;
   }

   @Override
   public int hashCode() {
      int hash = 5;

      hash = 61 * hash + (this.term != null ? this.term.hashCode() : 0);
      hash = 61 * hash + (this.scheme != null ? this.scheme.hashCode() : 0);
      hash = 61 * hash + (this.label != null ? this.label.hashCode() : 0);

      return hash + super.hashCode();
   }

   @Override
   public boolean equals(Object obj) {
      if (obj == null || getClass() != obj.getClass()) {
         return false;
      }

      final CategoryImpl other = (CategoryImpl) obj;

      if ((this.term == null) ? (other.term != null) : !this.term.equals(other.term)) {
         return false;
      }

      if ((this.scheme == null) ? (other.scheme != null) : !this.scheme.equals(other.scheme)) {
         return false;
      }

      if ((this.label == null) ? (other.label != null) : !this.label.equals(other.label)) {
         return false;
      }

      return super.equals(obj);
   }
}
