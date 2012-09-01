package org.atomnuke.atom.model.builder;

import org.atomnuke.atom.model.AtomCommonAtributes;
import org.atomnuke.atom.model.impl.SimpleContent;

/**
 *
 * @author zinic
 */
public abstract class SimpleContentBuilder<T extends SimpleContentBuilder, B extends AtomCommonAtributes, C extends SimpleContent> extends AtomConstructBuilderImpl<T, B, C> implements ValueBuilder<T> {

   private final StringBuilder valueBuilder;

   protected SimpleContentBuilder(Class<T> builderType, C atomConstruct) {
      super(builderType, atomConstruct);

      valueBuilder = new StringBuilder();
   }

   @Override
   public T setValue(String value) {
      valueBuilder.setLength(0);

      return appendValue(value);
   }

   @Override
   public final T appendValue(String value) {
      construct().setValue(valueBuilder.append(value).toString());

      return builder();
   }
}
