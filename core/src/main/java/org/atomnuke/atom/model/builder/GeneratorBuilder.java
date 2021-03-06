package org.atomnuke.atom.model.builder;

import org.atomnuke.atom.model.Generator;

/**
 *
 * @author zinic
 */
public class GeneratorBuilder extends SimpleContentBuilder<GeneratorBuilder, Generator, GeneratorImpl> {

   public GeneratorBuilder() {
      super(GeneratorBuilder.class, new GeneratorImpl());
   }

   public GeneratorBuilder(Generator copyConstruct) {
      super(GeneratorBuilder.class, new GeneratorImpl(), copyConstruct);

      if (copyConstruct != null) {
         if (copyConstruct.uri() != null) {
            setUri(copyConstruct.uri());
         }

         if (copyConstruct.version() != null) {
            setVersion(copyConstruct.version());
         }
      }
   }

   public final GeneratorBuilder setUri(String uri) {
      construct().setUri(uri);
      return this;
   }

   public final GeneratorBuilder setVersion(String version) {
      construct().setVersion(version);
      return this;
   }
}
