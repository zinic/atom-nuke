package org.atomnuke.atom.io;

import org.atomnuke.atom.model.Entry;
import org.atomnuke.atom.model.Feed;

/**
 *
 * @author zinic
 */
public interface ReaderResult {

   boolean isEntry();

   boolean isFeed();

   Feed getFeed();

   Entry getEntry();
}
