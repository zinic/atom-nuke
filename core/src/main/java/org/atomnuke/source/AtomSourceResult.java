package org.atomnuke.source;

import org.atomnuke.atom.model.Entry;
import org.atomnuke.atom.model.Feed;

/**
 *
 * @author zinic
 */
public interface AtomSourceResult {

   boolean isEmpty();

   boolean isFeedPage();

   Feed feed();

   Entry entry();
}
