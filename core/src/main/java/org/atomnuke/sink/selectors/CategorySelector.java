package org.atomnuke.sink.selectors;

import java.util.Collection;
import org.atomnuke.atom.model.Category;
import org.atomnuke.sink.eps.selector.EntrySelector;

/**
 *
 * @author zinic
 */
public interface CategorySelector extends EntrySelector {

   void addCategory(Category cat);

   boolean hasCategory(Category categoryToFind);

   Collection<Category> interestedCategories();

   boolean removeCategory(Category categoryToFind);
}
