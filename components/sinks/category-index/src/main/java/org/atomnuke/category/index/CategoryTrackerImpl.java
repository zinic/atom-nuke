package org.atomnuke.category.index;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.atomnuke.atom.model.Category;

/**
 *
 * @author zinic
 */
public class CategoryTrackerImpl implements CategoryTracker {

   private final Map<String, Category> categories;

   public CategoryTrackerImpl() {
      categories = new HashMap<String, Category>();
   }

   @Override
   public synchronized List<Category> getCategories() {
      return new LinkedList<Category>(categories.values());
   }

   @Override
   public synchronized Category getCategory(String key) {
      return categories.get(key);
   }

   @Override
   public synchronized void setCategory(String key, Category valueToAdd) {
      categories.put(key, valueToAdd);
   }
}
