package org.atomnuke.atom.model.builder;

import org.atomnuke.atom.model.Type;
import org.atomnuke.atom.model.TypedContent;

/**
 *
 * @author zinic
 */
public abstract class TypedContentBuilder<T extends TypedContentBuilder, B extends TypedContent> extends SimpleContentBuilder<T, B, TypedContentImpl> {

   protected TypedContentBuilder(Class<T> builderType) {
      super(builderType, new TypedContentImpl());
   }

   protected TypedContentBuilder(Class<T> builderType, B copyConstruct) {
      super(builderType, new TypedContentImpl(), copyConstruct);

      if (copyConstruct != null) {
         if (copyConstruct.type() != null) {
            setType(copyConstruct.type());
         }
      }
   }

   public final T setType(Type type) {
      construct().setType(type);
      return builder();
   }
}
