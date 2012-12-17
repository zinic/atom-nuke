package org.atomnuke.task.polling;

import org.atomnuke.util.TimeValue;

/**
 * A polling controller meters a task's execution speed.
 * 
 * @author zinic
 */
public interface PollingController {
   
   /**
    * Checks to see if the task should be polled.
    * 
    * @param now A time value that represents when this call was decided on. This may be used for scheduling purposes within the polling controller
    * @return 
    */
   boolean shouldPoll(TimeValue now);

   /**
    * Notifies the task that it has been scheduled and to set its internal state for the next polling interval.
    */
   void taskScheduled(TaskFuture scheduleFuture);
}
