package org.atomnuke.atom.model.builder;

import org.atomnuke.atom.model.Contributor;

/**
 *
 * @author zinic
 */
public class ContributorBuilder extends PersonConstructBuilder<ContributorBuilder, Contributor> {

   public ContributorBuilder() {
      super(ContributorBuilder.class);
   }

   public ContributorBuilder(Contributor copyConstruct) {
      super(ContributorBuilder.class, copyConstruct);
   }
}
