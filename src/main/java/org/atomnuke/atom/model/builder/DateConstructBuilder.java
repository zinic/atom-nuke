package org.atomnuke.atom.model.builder;

import org.atomnuke.atom.model.DateConstruct;
import org.atomnuke.atom.model.impl.DateConstructImpl;

/**
 *
 * @author zinic
 */
public abstract class DateConstructBuilder<T extends DateConstructBuilder, B extends DateConstruct> extends SimpleContentBuilder<T, B, DateConstructImpl> {

   protected DateConstructBuilder(Class<T> builderClass) {
      super(builderClass, new DateConstructImpl());
   }

   protected DateConstructBuilder(Class<T> builderClass, B copyConstruct) {
      super(builderClass, new DateConstructImpl(), copyConstruct);
   }
}
