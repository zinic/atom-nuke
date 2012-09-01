package org.atomnuke.atom.model.builder;

import org.atomnuke.atom.model.Type;
import org.atomnuke.atom.model.TypedContent;
import org.atomnuke.atom.model.impl.TypedContentImpl;

/**
 *
 * @author zinic
 */
public abstract class TypedContentBuilder<T extends TypedContentBuilder, B extends TypedContent> extends SimpleContentBuilder<T, B, TypedContentImpl> {

   protected TypedContentBuilder(Class<T> builderType) {
      super(builderType, new TypedContentImpl());
   }

   public T setType(Type type) {
      construct().setType(type);
      return builder();
   }
}
