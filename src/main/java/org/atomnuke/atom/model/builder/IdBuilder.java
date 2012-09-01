package org.atomnuke.atom.model.builder;

import org.atomnuke.atom.model.Id;
import org.atomnuke.atom.model.impl.TextContent;

/**
 *
 * @author zinic
 */
public class IdBuilder extends SimpleContentBuilder<IdBuilder, Id, TextContent> {

   public IdBuilder() {
      super(IdBuilder.class, new TextContent());
   }

   public IdBuilder(Id copyConstruct) {
      super(IdBuilder.class, new TextContent(), copyConstruct);
   }
}
