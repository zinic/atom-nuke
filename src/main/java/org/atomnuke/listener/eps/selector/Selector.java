package org.atomnuke.listener.eps.selector;

import org.atomnuke.atom.model.Entry;
import org.atomnuke.atom.model.Feed;

/**
 *
 * @author zinic
 */
public interface Selector {

   SelectorResult select(Feed feed);

   SelectorResult select(Entry entry);
}
