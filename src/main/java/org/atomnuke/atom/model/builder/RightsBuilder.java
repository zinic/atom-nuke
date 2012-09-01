package org.atomnuke.atom.model.builder;

import org.atomnuke.atom.model.Rights;

/**
 *
 * @author zinic
 */
public class RightsBuilder extends TypedContentBuilder<RightsBuilder, Rights> {

   public RightsBuilder() {
      super(RightsBuilder.class);
   }

   public RightsBuilder(Rights copyConstruct) {
      super(RightsBuilder.class, copyConstruct);
   }
}
