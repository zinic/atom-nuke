package net.jps.nuke.atom;

import net.jps.nuke.atom.model.Entry;
import net.jps.nuke.atom.model.Feed;

/**
 *
 * @author zinic
 */
public interface ParserResult {

   Feed getFeed();

   Entry getEntry();
}
