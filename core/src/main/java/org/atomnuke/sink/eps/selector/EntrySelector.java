package org.atomnuke.sink.eps.selector;

import org.atomnuke.atom.model.Entry;

/**
 *
 * @author zinic
 */
public interface EntrySelector {

   SelectorResult select(Entry entry);
}
