package net.jps.nuke.listener;

import net.jps.nuke.atom.model.Entry;
import net.jps.nuke.atom.model.Feed;
import net.jps.nuke.service.Service;

/**
 *
 * @author zinic
 */
public interface AtomListener extends Service {

   ListenerResult entry(Entry entry) throws AtomListenerException;

   ListenerResult feedPage(Feed page) throws AtomListenerException;
}
