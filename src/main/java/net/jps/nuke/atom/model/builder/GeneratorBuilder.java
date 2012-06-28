package net.jps.nuke.atom.model.builder;

import java.net.URI;
import net.jps.nuke.atom.model.Generator;
import net.jps.nuke.atom.model.impl.GeneratorImpl;

/**
 *
 * @author zinic
 */
public class GeneratorBuilder extends GeneratorImpl {

   public GeneratorBuilder newBuilder() {
      return new GeneratorBuilder();
   }

   protected GeneratorBuilder() {
   }

   public Generator build() {
      return this;
   }

   public void setUri(String uri) {
      this.uri = uri;
   }

   public void setVersion(String version) {
      this.version = version;
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
