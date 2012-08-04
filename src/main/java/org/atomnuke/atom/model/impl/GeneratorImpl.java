package org.atomnuke.atom.model.impl;

import org.atomnuke.atom.model.Generator;

/**
 *
 * @author zinic
 */
public abstract class GeneratorImpl extends AtomCommonAttributesImpl implements Generator {

   protected String uri;
   protected String version;
   protected StringBuilder value;

   public String uri() {
      return uri;
   }

   public String version() {
      return version;
   }

   public String value() {
      return value.toString();
   }
}
