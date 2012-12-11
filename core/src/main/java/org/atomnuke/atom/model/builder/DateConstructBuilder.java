package org.atomnuke.atom.model.builder;

import java.util.Calendar;
import javax.xml.bind.DatatypeConverter;
import org.atomnuke.atom.model.DateConstruct;

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

   public final T setValue(Calendar calendar) {
      setValue(DatatypeConverter.printDateTime(calendar));
      
      return builder();
   }
}
