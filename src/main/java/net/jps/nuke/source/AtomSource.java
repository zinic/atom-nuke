package net.jps.nuke.source;

import net.jps.nuke.task.lifecycle.TaskLifeCycle;

/**
 *
 * @author zinic
 */
public interface AtomSource extends TaskLifeCycle {

   AtomSourceResult poll() throws AtomSourceException;
}
