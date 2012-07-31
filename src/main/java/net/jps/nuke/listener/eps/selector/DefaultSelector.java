package net.jps.nuke.listener.eps.selector;

import net.jps.nuke.atom.model.Entry;
import net.jps.nuke.atom.model.Feed;

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
