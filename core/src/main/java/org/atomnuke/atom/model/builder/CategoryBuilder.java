package org.atomnuke.atom.model.builder;

import org.atomnuke.atom.model.Category;

/**
 *
 * @author zinic
 */
public class CategoryBuilder extends AtomConstructBuilderImpl<CategoryBuilder, Category, CategoryImpl> {

   public CategoryBuilder() {
      super(CategoryBuilder.class, new CategoryImpl());
   }

   public CategoryBuilder(Category copyConstruct) {
      super(CategoryBuilder.class, new CategoryImpl(), copyConstruct);

      if (copyConstruct != null) {
         if (copyConstruct.term() != null) {
            setTerm(copyConstruct.term());
         }

         if (copyConstruct.label() != null) {
            setLabel(copyConstruct.label());
         }

         if (copyConstruct.scheme() != null) {
            setScheme(copyConstruct.scheme());
         }
      }
   }

   public final CategoryBuilder setTerm(String term) {
      construct().setTerm(term);
      return this;
   }

   public final CategoryBuilder setScheme(String scheme) {
      construct().setScheme(scheme);
      return this;
   }

   public final CategoryBuilder setLabel(String label) {
      construct().setLabel(label);
      return this;
   }
}
