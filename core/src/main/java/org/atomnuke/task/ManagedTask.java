package org.atomnuke.task;

import org.atomnuke.task.lifecycle.TaskLifeCycle;
import org.atomnuke.util.TimeValue;

/**
 * A managed task represents a runnable scheduled delegate. This is the primary
 * descriptor of work in the Nuke scheduler. Every runnable delegate registered
 * to the Nuke scheduler is wrapped in this interface to make scheduling
 * simpler.
 *
 * @see TaskLifeCycle
 *
 * @author zinic
 */
public interface ManagedTask extends Runnable {

   /**
    * Returns the task handle that this managed task manages.
    *
    * @return the handle of the task that this managed task manages.
    */
   TaskHandle handle();

   /**
    * Returns the next polling interval desired by the task.
    *
    * @return the time value of the next desired polling interval.
    */
   TimeValue nextPollTime();

   /**
    * Notifies the task that it has been scheduled and to set its internal state
    * for the next polling interval.
    */
   void scheduleNext();
}
