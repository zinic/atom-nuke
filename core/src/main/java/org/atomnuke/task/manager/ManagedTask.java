package org.atomnuke.task.manager;

import java.util.UUID;
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
public interface ManagedTask extends TaskLifeCycle, Runnable {

   /**
    * All tasks registered with the Nuke scheduler are required to have a unique
    * ID in the form of a UUID.
    *
    * @return the tasks ID UUID.
    */
   UUID id();

   /**
    * Returns whether or not the task has been canceled.
    *
    * @return true if the task has been canceled, false otherwise.
    */
   boolean canceled();

   /**
    * Cancels the task.
    */
   void cancel();

   /**
    * Returns the next polling interval desired by the task.
    *
    * @return the time value of the next desired polling interval
    */
   TimeValue nextPollTime();

   /**
    * Notifies the task that it has been scheduled and to set its internal state
    * for the next polling interval.
    */
   void scheduled();
}
