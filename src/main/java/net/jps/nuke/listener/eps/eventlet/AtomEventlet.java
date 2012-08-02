package net.jps.nuke.listener.eps.eventlet;

import net.jps.nuke.atom.model.Entry;
import net.jps.nuke.task.lifecycle.TaskLifeCycle;

/**
 *
 * @author zinic
 */
public interface AtomEventlet extends TaskLifeCycle {

   void entry(Entry entry) throws AtomEventletException;
}
