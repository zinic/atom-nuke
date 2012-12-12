package org.atomnuke.task;

import org.atomnuke.task.polling.PollingController;

/**
 * A managed task represents a runnable scheduled delegate. This is the primary descriptor of work in the Nuke scheduler. Every runnable delegate registered to
 * the Nuke scheduler is wrapped in this interface to make scheduling simpler.
 *
 * @see TaskLifeCycle
 *
 * @author zinic
 */
public interface ManagedTask<T extends TaskHandle> extends Runnable {

   /**
    * Returns the task handle that this managed task manages.
    *
    * @return the handle of the task that this managed task manages.
    */
   T handle();

   /**
    * Returns the controller object that will tell the system when this task may be polled.
    *
    * @return
    */
   PollingController pollingController();
}
