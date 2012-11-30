package org.atomnuke.pubsub.sub;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.atomnuke.atom.model.Category;
import org.atomnuke.atom.model.Entry;
import org.atomnuke.pubsub.api.type.SubscriptionCategory;
import org.atomnuke.sink.eps.selector.EntrySelector;
import org.atomnuke.sink.eps.selector.SelectorResult;

/**
 *
 * @author zinic
 */
public class RegexCategorySelector implements EntrySelector {

   private final Set<Pattern> compiledPatterns;

   public RegexCategorySelector() {
      compiledPatterns = new HashSet<Pattern>();
   }

   public synchronized void selectOn(SubscriptionCategory category) {
      final String searchCat = subscriptionCategoryToString(category);
      
      //TODO: if we're doing translation of the search category than chances are we're going to want to sanitize it better - for now, be lazy
      String saitizedString = searchCat.replace(".", "\\.");
      saitizedString = saitizedString.replace("*", ".*");

      final Pattern pattern = Pattern.compile(saitizedString);
      compiledPatterns.add(pattern);
   }

   private static String subscriptionCategoryToString(SubscriptionCategory cat) {
      final StringBuilder searchCatBuilder = new StringBuilder();

      if (!StringUtils.isBlank(cat.getScheme())) {
         searchCatBuilder.append(cat.getScheme());
      }

      if (!StringUtils.isBlank(cat.getTerm())) {
         searchCatBuilder.append(cat.getTerm());
      }

      return searchCatBuilder.toString();
   }

   private static String categoryToString(Category cat) {
      final StringBuilder searchCatBuilder = new StringBuilder();

      if (!StringUtils.isBlank(cat.scheme())) {
         searchCatBuilder.append(cat.scheme());
      }

      if (!StringUtils.isBlank(cat.term())) {
         searchCatBuilder.append(cat.term());
      }

      return searchCatBuilder.toString();
   }

   @Override
   public synchronized SelectorResult select(Entry entry) {
      for (Category cat : entry.categories()) {
         final String searchCat = categoryToString(cat);

         if (StringUtils.isNotBlank(searchCat)) {
            for (Pattern pattern : compiledPatterns) {
               if (pattern.matcher(searchCat).matches()) {
                  return SelectorResult.PROCESS;
               }
            }
         }
      }

      return SelectorResult.PASS;
   }
}
