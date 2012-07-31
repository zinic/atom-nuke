package net.jps.nuke.atom.model.builder;

import java.net.URI;
import net.jps.nuke.atom.model.impl.LangAwareTextElement;

/**
 *
 * @author zinic
 */
public class LangAwareTextElementBuilder extends LangAwareTextElement implements ValueBuilder<LangAwareTextElementBuilder> {

   public LangAwareTextElementBuilder() {
      value = new StringBuilder();
   }
   
   @Override
   public LangAwareTextElementBuilder setValue(String value) {
      this.value = new StringBuilder(value);
      return this;
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
