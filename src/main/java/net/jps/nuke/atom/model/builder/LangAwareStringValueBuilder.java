package net.jps.nuke.atom.model.builder;

import java.net.URI;
import net.jps.nuke.atom.model.impl.LangAwareStringValue;

/**
 *
 * @author zinic
 */
public class LangAwareStringValueBuilder extends LangAwareStringValue {

   public LangAwareStringValueBuilder newBuilder() {
      return new LangAwareStringValueBuilder();
   }

   protected LangAwareStringValueBuilder() {
   }

   public LangAwareStringValue build() {
      return this;
   }

   public void setValue(String value) {
      this.value = value;
   }

   public void setBase(URI base) {
      this.base = base;
   }

   public void setLang(String lang) {
      this.lang = lang;
   }
}
