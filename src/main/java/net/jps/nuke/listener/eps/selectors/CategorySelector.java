package net.jps.nuke.listener.eps.selectors;

import net.jps.nuke.atom.model.Category;
import net.jps.nuke.atom.model.Entry;
import net.jps.nuke.atom.model.Feed;
import net.jps.nuke.listener.eps.handler.Selector;
import net.jps.nuke.listener.eps.handler.SelectorResult;

/**
 *
 * @author zinic
 */
public class CategorySelector implements Selector {

   private final String[] categoryTerms;

   public CategorySelector(String[] categoryTerms) {
      this.categoryTerms = categoryTerms;
   }

   @Override
   public SelectorResult select(Feed feed) {
      for (Category category : feed.categories()) {
         if (category.term() != null) {
            for (String targetCategory : categoryTerms) {
               if (targetCategory.equals(category.term())) {
                  return SelectorResult.PROCESS;
               }
            }
         }
      }

      return SelectorResult.PASS;
   }

   @Override
   public SelectorResult select(Entry entry) {
      for (Category category : entry.categories()) {
         if (category.term() != null) {
            for (String targetCategory : categoryTerms) {
               if (targetCategory.equals(category.term())) {
                  return SelectorResult.PROCESS;
               }
            }
         }
      }

      return SelectorResult.PASS;
   }
}
