package org.atomnuke.atom.model.impl;

import java.net.URI;
import org.atomnuke.atom.model.AtomCommonAtributes;

/**
 *
 * @author zinic
 */
public abstract class AtomCommonAttributesImpl implements AtomCommonAtributes {

   private String lang;
   private URI base;

   public void setBase(URI base) {
      this.base = base;
   }

   public void setLang(String lang) {
      this.lang = lang;
   }

   @Override
   public URI base() {
      return base;
   }

   @Override
   public String lang() {
      return lang;
   }

   @Override
   public int hashCode() {
      int hash = 3;

      hash = 67 * hash + (this.lang != null ? this.lang.hashCode() : 0);
      hash = 67 * hash + (this.base != null ? this.base.hashCode() : 0);

      return hash;
   }

   @Override
   public boolean equals(Object obj) {
      if (obj == null || getClass() != obj.getClass()) {
         return false;
      }

      final AtomCommonAttributesImpl other = (AtomCommonAttributesImpl) obj;

      if ((this.lang == null) ? (other.lang != null) : !this.lang.equals(other.lang)) {
         return false;
      }

      if (this.base != other.base && (this.base == null || !this.base.equals(other.base))) {
         return false;
      }

      return true;
   }
}
