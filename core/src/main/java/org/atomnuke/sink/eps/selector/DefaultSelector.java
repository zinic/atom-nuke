package org.atomnuke.sink.eps.selector;

import org.atomnuke.atom.model.Entry;

/**
 * Default selector processes all events.
 *
 * @author zinic
 */
public final class DefaultSelector implements EntrySelector {

   private static final EntrySelector INSTANCE = new DefaultSelector();

   public static EntrySelector instance() {
      return INSTANCE;
   }

   @Override
   public SelectorResult select(Entry entry) {
      return SelectorResult.PROCESS;
   }
}
