package org.atomnuke.atom.model.impl;

import java.net.URI;
import org.atomnuke.atom.model.AtomCommonAtributes;

/**
 *
 * @author zinic
 */
public abstract class AtomCommonAttributesImpl implements AtomCommonAtributes {

   protected URI base;
   protected String lang;

   public URI base() {
      return base;
   }

   public String lang() {
      return lang;
   }
}
