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
}
