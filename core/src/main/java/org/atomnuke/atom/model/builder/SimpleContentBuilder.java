package org.atomnuke.atom.model.builder;

import org.atomnuke.atom.model.SimpleContent;
import org.atomnuke.atom.model.impl.SimpleContentImpl;

/**
 *
 * @author zinic
 */
public abstract class SimpleContentBuilder<T extends SimpleContentBuilder, B extends SimpleContent, C extends SimpleContentImpl> extends AtomConstructBuilderImpl<T, B, C> implements ValueBuilder<T> {

   private final StringBuilder valueBuilder;

   protected SimpleContentBuilder(Class<T> builderType, C atomConstruct) {
      super(builderType, atomConstruct);

      valueBuilder = new StringBuilder();
   }

   protected SimpleContentBuilder(Class<T> builderType, C atomConstruct, B copyConstruct) {
      super(builderType, atomConstruct, copyConstruct);

      valueBuilder = new StringBuilder();

      if (copyConstruct != null) {
         appendValue(copyConstruct.toString());
      }
   }

   @Override
   public final T setValue(String value) {
      valueBuilder.setLength(0);

      return appendValue(value);
   }

   @Override
   public final T appendValue(String value) {
      valueBuilder.append(value);
      construct().setValue(valueBuilder.toString());

      return builder();
   }
}
