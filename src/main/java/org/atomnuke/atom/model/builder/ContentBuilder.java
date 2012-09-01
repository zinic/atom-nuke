package org.atomnuke.atom.model.builder;

import org.atomnuke.atom.model.Content;
import org.atomnuke.atom.model.impl.ContentImpl;

/**
 *
 * @author zinic
 */
public class ContentBuilder extends SimpleContentBuilder<ContentBuilder, Content, ContentImpl> {

   public ContentBuilder() {
      super(ContentBuilder.class, new ContentImpl());
   }

   public ContentBuilder setType(String type) {
      construct().setType(type);
      return this;
   }

   public ContentBuilder setSrc(String src) {
      construct().setSrc(src);
      return this;
   }
}
