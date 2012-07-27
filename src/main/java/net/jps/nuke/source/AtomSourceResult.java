package net.jps.nuke.source;

import net.jps.nuke.atom.model.Entry;
import net.jps.nuke.atom.model.Feed;

/**
 *
 * @author zinic
 */
public interface AtomSourceResult {

   boolean isFeedPage();

   Feed feed();

   Entry entry();
}
