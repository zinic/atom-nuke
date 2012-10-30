package org.atomnuke.sink.eps.selector;

import org.atomnuke.atom.model.Entry;
import org.atomnuke.atom.model.Feed;

/**
 * Default selector processes all events.
 *
 * @author zinic
 */
public final class DefaultSelector implements Selector {

   private static final Selector INSTANCE = new DefaultSelector();

   public static Selector instance() {
      return INSTANCE;
   }

   @Override
   public SelectorResult select(Feed feed) {
      return SelectorResult.PROCESS;
   }

   @Override
   public SelectorResult select(Entry entry) {
      return SelectorResult.PROCESS;
   }
}
