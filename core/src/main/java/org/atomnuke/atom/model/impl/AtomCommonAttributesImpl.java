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
   public final boolean equals(Object obj) {
      if (obj == null || getClass() != obj.getClass()) {
         return false;
      }

      return hashCode() == obj.hashCode();
   }
}
