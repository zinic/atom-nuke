package org.atomnuke.listener.eps.selector;

import org.atomnuke.atom.model.Entry;
import org.atomnuke.atom.model.Feed;

/**
 * Default selector processes all events.
 *
 * @author zinic
 */
public class DefaultSelector implements Selector {

   public static final Selector INSTANCE = new DefaultSelector();

   private DefaultSelector() {
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
