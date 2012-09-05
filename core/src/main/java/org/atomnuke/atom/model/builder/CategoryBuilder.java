package org.atomnuke.atom.model.builder;

import org.atomnuke.atom.model.Category;
import org.atomnuke.atom.model.impl.CategoryImpl;

/**
 *
 * @author zinic
 */
public class CategoryBuilder extends AtomConstructBuilderImpl<CategoryBuilder, Category, CategoryImpl> {

   public CategoryBuilder() {
      super(CategoryBuilder.class, new CategoryImpl());
   }

   public CategoryBuilder setTerm(String term) {
      construct().setTerm(term);
      return this;
   }

   public CategoryBuilder setScheme(String scheme) {
      construct().setScheme(scheme);
      return this;
   }

   public CategoryBuilder setLabel(String label) {
      construct().setLabel(label);
      return this;
   }
}
