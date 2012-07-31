package net.jps.nuke.atom.model.builder;

import java.net.URI;
import net.jps.nuke.atom.model.Type;
import net.jps.nuke.atom.model.impl.TextConstructImpl;

/**
 *
 * @author zinic
 */
public class TextConstructBuilder extends TextConstructImpl implements ValueBuilder<TextConstructBuilder> {

   public TextConstructBuilder() {
      this.value = new StringBuilder();
   }

   public TextConstructBuilder setType(Type type) {
      this.type = type;
      return this;
   }

   @Override
   public TextConstructBuilder setValue(String value) {
      this.value = new StringBuilder(value);
      return this;
   }

   @Override
   public TextConstructBuilder appendValue(String value) {
      this.value.append(value);
      return this;
   }

   public TextConstructBuilder setBase(URI base) {
      this.base = base;
      return this;
   }

   public TextConstructBuilder setLang(String lang) {
      this.lang = lang;
      return this;
   }
}
