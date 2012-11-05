package org.atomnuke.task;

import org.atomnuke.util.TimeValue;

/**
 *
 * @author zinic
 */
public interface PollingTaskHandle extends TaskHandle {

   /**
    * Returns the polling interval of this task.
    *
    * @return the time interval representing the desired polling interval of
    * this task.
    */
   TimeValue scheduleInterval();
}
