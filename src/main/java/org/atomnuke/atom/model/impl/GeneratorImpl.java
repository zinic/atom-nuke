package org.atomnuke.atom.model.impl;

import org.atomnuke.atom.model.Generator;

/**
 *
 * @author zinic
 */
public class GeneratorImpl extends SimpleContentImpl implements Generator {

   private String uri, version;

   public void setUri(String uri) {
      this.uri = uri;
   }

   public void setVersion(String version) {
      this.version = version;
   }

   @Override
   public String uri() {
      return uri;
   }

   @Override
   public String version() {
      return version;
   }
}
