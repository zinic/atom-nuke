package org.atomnuke.atom.model.builder;

import org.atomnuke.atom.model.Updated;

/**
 *
 * @author zinic
 */
public class UpdatedBuilder extends DateConstructBuilder<UpdatedBuilder, Updated> {

   public UpdatedBuilder() {
      super(UpdatedBuilder.class);
   }

   public UpdatedBuilder(Updated copyConstruct) {
      super(UpdatedBuilder.class, copyConstruct);
   }
}
