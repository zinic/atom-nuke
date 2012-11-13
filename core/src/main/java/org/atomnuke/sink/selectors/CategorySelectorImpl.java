package org.atomnuke.sink.selectors;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.atomnuke.atom.model.Category;
import org.atomnuke.atom.model.Entry;
import org.atomnuke.sink.eps.selector.SelectorResult;

/**
 *
 * @author zinic
 */
public class CategorySelectorImpl implements CategorySelector {

   public static boolean categoriesIntersect(List<Category> termsToSearchThrough, List<Category> categoriesToFind) {
      for (Category categoryToFind : categoriesToFind) {
         if (hasCategoryTerm(termsToSearchThrough, categoryToFind)) {
            return true;
         }
      }

      return false;
   }

   public static boolean hasCategoryTerm(List<Category> categoriesToSearch, Category categoryToFind) {
      for (Category targetCategory : categoriesToSearch) {
         final boolean schemesMatch = StringUtils.isBlank(targetCategory.scheme()) ? StringUtils.isBlank(categoryToFind.scheme()) : targetCategory.scheme().equals(categoryToFind.scheme());
         final boolean termsMatch = StringUtils.isBlank(targetCategory.term()) ? StringUtils.isBlank(categoryToFind.term()) : targetCategory.term().equals(categoryToFind.term());

         if (schemesMatch && termsMatch) {
            return true;
         }
      }

      return false;
   }

   private final List<Category> interestedCategories;

   public CategorySelectorImpl() {
      interestedCategories = new LinkedList<Category>();
   }

   @Override
   public synchronized void addCategory(Category cat) {
      interestedCategories.add(cat);
   }

   @Override
   public synchronized boolean removeCategory(Category categoryToFind) {
      for (Iterator<Category> categoryIterator = interestedCategories().iterator(); categoryIterator.hasNext();) {
         final Category interestedCategory = categoryIterator.next();

         if (interestedCategory.scheme().equals(categoryToFind.scheme()) && interestedCategory.term().equals(categoryToFind.term())) {
            categoryIterator.remove();
            return true;
         }
      }

      return false;
   }

   @Override
   public synchronized boolean hasCategory(Category categoryToFind) {
      for (Iterator<Category> categoryIterator = interestedCategories().iterator(); categoryIterator.hasNext();) {
         final Category interestedCategory = categoryIterator.next();

         if (interestedCategory.scheme().equals(categoryToFind.scheme()) && interestedCategory.term().equals(categoryToFind.term())) {
            return true;
         }
      }

      return false;
   }

   @Override
   public synchronized List<Category> interestedCategories() {
      return new LinkedList<Category>(interestedCategories);
   }

   @Override
   public SelectorResult select(Entry entry) {
      if (!interestedCategories.isEmpty() && categoriesIntersect(interestedCategories, entry.categories())) {
         return SelectorResult.PROCESS;
      }

      return SelectorResult.PASS;
   }
}
