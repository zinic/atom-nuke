package net.jps.nuke.listener;

import net.jps.nuke.atom.model.Entry;
import net.jps.nuke.atom.model.Feed;

/**
 *
 * @author zinic
 */
public interface AtomListener {

   void init();

   void destroy();

   ListenerResult readEntry(Entry entry);

   ListenerResult readPage(Feed page);
}
