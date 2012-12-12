package org.atomnuke.task.polling;

/**
 *
 * @author zinic
 */
public interface PollingController {
   
   boolean shouldPoll();

   /**
    * Notifies the task that it has been scheduled and to set its internal state for the next polling interval.
    */
   void taskScheduled(TaskFuture scheduleFuture);
}
