package org.atomnuke.listener.eps.eventlet;

import org.atomnuke.atom.model.Entry;
import org.atomnuke.task.context.AtomTaskContext;
import org.atomnuke.util.lifecycle.ResourceLifeCycle;

/**
 *
 * @author zinic
 */
public interface AtomEventlet extends ResourceLifeCycle<AtomTaskContext> {

   void entry(Entry entry) throws AtomEventletException;
}
