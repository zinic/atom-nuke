package net.jps.nuke.atom.model.builder;

import java.net.URI;
import net.jps.nuke.atom.model.impl.ContentImpl;

/**
 *
 * @author zinic
 */
public class ContentBuilder extends ContentImpl implements ValueBuilder<ContentBuilder> {

   public ContentBuilder() {
      value = new StringBuilder();
   }

   public ContentBuilder setType(String type) {
      this.type = type;
      return this;
   }

   public ContentBuilder setSrc(String src) {
      this.src = src;
      return this;
   }

   @Override
   public ContentBuilder setValue(String value) {
      this.value = new StringBuilder(value);
      return this;
   }

   @Override
   public ContentBuilder appendValue(String value) {
      this.value.append(value);
      return this;
   }

   public ContentBuilder setBase(URI base) {
      this.base = base;
      return this;
   }

   public ContentBuilder setLang(String lang) {
      this.lang = lang;
      return this;
   }
}
