package net.jps.nuke.listener.eps.handler;

import net.jps.nuke.atom.model.Entry;
import net.jps.nuke.atom.model.Feed;

/**
 *
 * @author zinic
 */
public interface Selector {

   SelectorResult select(Feed feed);

   SelectorResult select(Entry entry);
}
