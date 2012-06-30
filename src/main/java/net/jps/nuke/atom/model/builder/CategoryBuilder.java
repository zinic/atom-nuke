package net.jps.nuke.atom.model.builder;

import java.net.URI;
import net.jps.nuke.atom.model.Category;
import net.jps.nuke.atom.model.impl.CategoryImpl;

/**
 *
 * @author zinic
 */
public class CategoryBuilder extends CategoryImpl {

   public static CategoryBuilder newBuilder() {
      return new CategoryBuilder();
   }

   protected CategoryBuilder() {
   }

   public Category build() {
      return this;
   }

   public void setTerm(String term) {
      this.term = term;
   }

   public void setScheme(String scheme) {
      this.scheme = scheme;
   }

   public void setLabel(String label) {
      this.label = label;
   }

   public void setBase(URI base) {
      this.base = base;
   }

   public void setLang(String lang) {
      this.lang = lang;
   }
}
