package net.jps.nuke.atom.model.impl;

import net.jps.nuke.atom.model.Generator;

/**
 *
 * @author zinic
 */
public abstract class GeneratorImpl extends AtomCommonAttributesImpl implements Generator {

   protected String uri;
   protected String version;
   protected String value;

   public String uri() {
      return uri;
   }

   public String version() {
      return version;
   }

   public String value() {
      return value;
   }
}
