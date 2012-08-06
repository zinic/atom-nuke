package org.atomnuke.atom.model.builder;

import java.net.URI;
import org.atomnuke.atom.model.impl.LangAwareTextElement;

/**
 *
 * @author zinic
 */
public class LangAwareTextElementBuilder extends LangAwareTextElement implements ValueBuilder<LangAwareTextElementBuilder> {

   public LangAwareTextElementBuilder() {
      value = new StringBuilder();
   }

   public String getValue() {
      return value.toString();
   }

   public URI getBase() {
      return base;
   }

   public String getLang() {
      return lang;
   }

   @Override
   public LangAwareTextElementBuilder appendValue(String value) {
      this.value.append(value);
      return this;
   }

   public LangAwareTextElementBuilder setBase(URI base) {
      this.base = base;
      return this;
   }

   public LangAwareTextElementBuilder setLang(String lang) {
      this.lang = lang;
      return this;
   }
}
