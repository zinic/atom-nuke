package net.jps.nuke.atom.model.builder;

import java.net.URI;
import net.jps.nuke.atom.model.impl.LangAwareTextElement;

/**
 *
 * @author zinic
 */
public class LangAwareTextElementBuilder extends LangAwareTextElement {

   public static LangAwareTextElementBuilder newBuilder() {
      return new LangAwareTextElementBuilder();
   }

   protected LangAwareTextElementBuilder() {
      value = new StringBuilder();
   }

   public LangAwareTextElement build() {
      return this;
   }

   public void setValue(String value) {
      this.value = new StringBuilder(value);
   }

   public void appendValue(String value) {
      this.value.append(value);
   }

   public void setBase(URI base) {
      this.base = base;
   }

   public void setLang(String lang) {
      this.lang = lang;
   }
}
