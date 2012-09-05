package org.atomnuke.atom.model.builder;

import org.atomnuke.atom.model.Summary;

/**
 *
 * @author zinic
 */
public class SummaryBuilder extends TypedContentBuilder<SummaryBuilder, Summary> {

   public SummaryBuilder() {
      super(SummaryBuilder.class);
   }

   public SummaryBuilder(Summary copyConstruct) {
      super(SummaryBuilder.class, copyConstruct);
   }
}
