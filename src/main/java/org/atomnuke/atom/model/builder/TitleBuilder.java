package org.atomnuke.atom.model.builder;

import org.atomnuke.atom.model.Title;

/**
 *
 * @author zinic
 */
public class TitleBuilder extends TypedContentBuilder<TitleBuilder, Title> {

   public TitleBuilder() {
      super(TitleBuilder.class);
   }

   public TitleBuilder(Title copyConstruct) {
      super(TitleBuilder.class, copyConstruct);
   }
}
