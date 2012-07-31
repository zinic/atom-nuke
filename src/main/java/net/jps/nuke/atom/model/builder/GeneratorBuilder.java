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

   public GeneratorBuilder setUri(String uri) {
      this.uri = uri;
      return this;
   }

   public GeneratorBuilder setVersion(String version) {
      this.version = version;
      return this;
   }

   public GeneratorBuilder setValue(String value) {
      this.value = new StringBuilder(value);
      return this;
   }

   public GeneratorBuilder appendValue(String value) {
      this.value.append(value);
      return this;
   }

   public GeneratorBuilder setBase(URI base) {
      this.base = base;
      return this;
   }

   public GeneratorBuilder setLang(String lang) {
      this.lang = lang;
      return this;
   }
}
