package org.atomnuke.atom.model.builder;

import org.atomnuke.atom.model.Icon;
import org.atomnuke.atom.model.impl.TextContent;

/**
 *
 * @author zinic
 */
public class IconBuilder extends SimpleContentBuilder<IconBuilder, Icon, TextContent> {

   public IconBuilder() {
      super(IconBuilder.class, new TextContent());
   }

   public IconBuilder(Icon copyMe) {
      super(IconBuilder.class, new TextContent(), copyMe);
   }
}