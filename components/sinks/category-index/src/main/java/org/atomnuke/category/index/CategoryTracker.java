package org.atomnuke.category.index;

import java.util.List;
import org.atomnuke.atom.model.Category;

/**
 *
 * @author zinic
 */
public interface CategoryTracker {

   List<Category> getCategories();

   Category getCategory(String key);

   void setCategory(String key, Category valueToAdd);
}
