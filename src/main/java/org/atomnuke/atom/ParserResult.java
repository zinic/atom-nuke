package org.atomnuke.atom;

import org.atomnuke.atom.model.Entry;
import org.atomnuke.atom.model.Feed;

/**
 *
 * @author zinic
 */
public interface ParserResult {

   Feed getFeed();

   Entry getEntry();
}
