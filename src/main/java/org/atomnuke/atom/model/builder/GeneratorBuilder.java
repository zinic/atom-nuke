package org.atomnuke.atom.model.builder;

import org.atomnuke.atom.model.Generator;
import org.atomnuke.atom.model.impl.GeneratorImpl;

/**
 *
 * @author zinic
 */
public class GeneratorBuilder extends SimpleContentBuilder<GeneratorBuilder, Generator, GeneratorImpl> {

   public GeneratorBuilder() {
      super(GeneratorBuilder.class, new GeneratorImpl());
   }

   public GeneratorBuilder setUri(String uri) {
      construct().setUri(uri);
      return this;
   }

   public GeneratorBuilder setVersion(String version) {
      construct().setVersion(version);
      return this;
   }
}
