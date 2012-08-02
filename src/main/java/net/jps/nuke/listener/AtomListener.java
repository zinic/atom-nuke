package net.jps.nuke.listener;

import net.jps.nuke.atom.model.Entry;
import net.jps.nuke.atom.model.Feed;
import net.jps.nuke.task.lifecycle.TaskLifeCycle;

/**
 *
 * @author zinic
 */
public interface AtomListener extends TaskLifeCycle {

   ListenerResult entry(Entry entry) throws AtomListenerException;

   ListenerResult feedPage(Feed page) throws AtomListenerException;
}
