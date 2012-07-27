package net.jps.nuke.listener;

import net.jps.nuke.atom.model.Entry;
import net.jps.nuke.atom.model.Feed;

/**
 *
 * @author zinic
 */
public interface AtomListener {

   void init() throws AtomListenerException;

   void destroy() throws AtomListenerException;

   ListenerResult readEntry(Entry entry) throws AtomListenerException;

   ListenerResult readPage(Feed page) throws AtomListenerException;
}
