package org.atomnuke.atom.model.builder;

import org.atomnuke.atom.model.Content;

/**
 *
 * @author zinic
 */
public class ContentBuilder extends SimpleContentBuilder<ContentBuilder, Content, ContentImpl> {

   public ContentBuilder() {
      super(ContentBuilder.class, new ContentImpl());
   }

   public ContentBuilder(Content copyConstruct) {
      super(ContentBuilder.class, new ContentImpl(), copyConstruct);

      if (copyConstruct != null) {
         if (copyConstruct.src() != null) {
            setSrc(copyConstruct.src());
         }

         if (copyConstruct.type() != null) {
            setType(copyConstruct.type());
         }
      }
   }

   public final ContentBuilder setType(String type) {
      construct().setType(type);
      return this;
   }

   public final ContentBuilder setSrc(String src) {
      construct().setSrc(src);
      return this;
   }
}
