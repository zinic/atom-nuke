package org.atomnuke.atom.model.builder;

import org.atomnuke.atom.model.Published;

/**
 *
 * @author zinic
 */
public class PublishedBuilder extends DateConstructBuilder<PublishedBuilder, Published> {

   public PublishedBuilder() {
      super(PublishedBuilder.class);
   }

   public PublishedBuilder(Published copyConstruct) {
      super(PublishedBuilder.class, copyConstruct);
   }
}
