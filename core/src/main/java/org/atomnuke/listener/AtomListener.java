package org.atomnuke.listener;

import org.atomnuke.atom.model.Entry;
import org.atomnuke.atom.model.Feed;
import org.atomnuke.task.context.AtomTaskContext;
import org.atomnuke.util.lifecycle.ResourceLifeCycle;

/**
 *
 * @author zinic
 */
public interface AtomListener extends ResourceLifeCycle<AtomTaskContext> {

   ListenerResult entry(Entry entry) throws AtomListenerException;

   ListenerResult feedPage(Feed page) throws AtomListenerException;
}
