package org.atomnuke.atom.model.builder;

import java.net.URI;
import org.atomnuke.atom.model.AtomCommonAtributes;
import org.atomnuke.atom.model.impl.AtomCommonAttributesImpl;

/**
 *
 * @author zinic
 */
public abstract class AtomConstructBuilderImpl<T extends AtomConstructBuilder, B extends AtomCommonAtributes, C extends AtomCommonAttributesImpl> implements AtomConstructBuilder<T, B> {

   private final Class<T> builderType;
   private final C atomConstruct;

   public AtomConstructBuilderImpl(Class<T> builderType, C atomConstruct) {
      this.atomConstruct = atomConstruct;
      this.builderType = builderType;
   }

   protected final T builder() {
      return builderType.cast(this);
   }

   protected final C construct() {
      return atomConstruct;
   }

   @Override
   public B build() {
      return (B) construct();
   }

   @Override
   public final T setBase(URI base) {
      construct().setBase(base);

      return builder();
   }

   @Override
   public final T setLang(String lang) {
      construct().setLang(lang);

      return builder();
   }
}
