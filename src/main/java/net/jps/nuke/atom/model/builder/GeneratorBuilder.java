package net.jps.nuke.atom.model.builder;

import java.net.URI;
import net.jps.nuke.atom.model.impl.GeneratorImpl;

/**
 *
 * @author zinic
 */
public class GeneratorBuilder extends GeneratorImpl {

   public GeneratorBuilder() {
      value = new StringBuilder();
   }

   public void setUri(String uri) {
      this.uri = uri;
   }

   public void setVersion(String version) {
      this.version = version;
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
