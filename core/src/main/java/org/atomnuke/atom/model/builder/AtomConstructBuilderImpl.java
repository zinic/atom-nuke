package org.atomnuke.atom.model.builder;

import java.net.URI;
import org.atomnuke.atom.model.AtomCommonAtributes;

/**
 *
 * @author zinic
 */
public abstract class AtomConstructBuilderImpl<T extends AtomConstructBuilder, B extends AtomCommonAtributes, C extends AtomCommonAttributesImpl> implements AtomConstructBuilder<T, B> {

   private final Class<T> builderType;
   private final C atomConstruct;

   protected AtomConstructBuilderImpl(Class<T> builderType, C atomConstruct) {
      this.atomConstruct = atomConstruct;
      this.builderType = builderType;
   }

   protected AtomConstructBuilderImpl(Class<T> builderType, C atomConstruct, B copyConstruct) {
      this.atomConstruct = atomConstruct;
      this.builderType = builderType;

      if (copyConstruct != null) {
         if (copyConstruct.base() != null) {
            setBase(copyConstruct.base());
         }

         if (copyConstruct.lang() != null) {
            setLang(copyConstruct.lang());
         }
      }
   }

   protected final T builder() {
      return builderType.cast(this);
   }

   protected final C construct() {
      return atomConstruct;
   }

   @Override
   public final B build() {
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
