package net.jps.nuke.atom.model.builder;

import java.net.URI;
import net.jps.nuke.atom.model.Type;
import net.jps.nuke.atom.model.impl.TextConstructImpl;

/**
 *
 * @author zinic
 */
public class TextConstructBuilder extends TextConstructImpl {

   public TextConstructBuilder() {
      this.value = new StringBuilder();
   }

   public void setType(Type type) {
      this.type = type;
   }

   public StringBuilder getValueBuilder() {
      return value;
   }

   public void setBase(URI base) {
      this.base = base;
   }

   public void setLang(String lang) {
      this.lang = lang;
   }
}
