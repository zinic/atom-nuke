package org.atomnuke.source;

import org.atomnuke.task.lifecycle.TaskLifeCycle;

/**
 *
 * @author zinic
 */
public interface AtomSource extends TaskLifeCycle {

   AtomSourceResult poll() throws AtomSourceException;
}
