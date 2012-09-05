package org.atomnuke.listener.eps.eventlet;

import org.atomnuke.atom.model.Entry;
import org.atomnuke.task.lifecycle.TaskLifeCycle;

/**
 *
 * @author zinic
 */
public interface AtomEventlet extends TaskLifeCycle {

   void entry(Entry entry) throws AtomEventletException;
}
