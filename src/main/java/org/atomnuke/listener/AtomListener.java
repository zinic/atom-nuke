package org.atomnuke.listener;

import org.atomnuke.atom.model.Entry;
import org.atomnuke.atom.model.Feed;
import org.atomnuke.task.lifecycle.TaskLifeCycle;

/**
 *
 * @author zinic
 */
public interface AtomListener extends TaskLifeCycle {

   ListenerResult entry(Entry entry) throws AtomListenerException;

   ListenerResult feedPage(Feed page) throws AtomListenerException;
}
