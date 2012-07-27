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

   ListenerResult entry(Entry entry) throws AtomListenerException;

   ListenerResult feedPage(Feed page) throws AtomListenerException;
}
