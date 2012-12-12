package org.atomnuke.task.polling;

import org.atomnuke.task.TaskHandle;
import org.atomnuke.util.TimeValue;

/**
 *
 * @author zinic
 */
public class TimeIntervalPollingController extends AbstractPollingController {
   
   private final TimeValue scheduleInterval;
   private TimeValue lastRunTimestamp;
   
   public TimeIntervalPollingController(TaskHandle taskHandle, TimeValue scheduleInterval) {
      super(taskHandle);
      
      this.scheduleInterval = scheduleInterval;
      lastRunTimestamp = TimeValue.now();
   }
   
   private synchronized void scheduled() {
      lastRunTimestamp = TimeValue.now();
   }
   
   private synchronized TimeValue lastRuntime() {
      return lastRunTimestamp;
   }
   
   @Override
   public boolean shouldPoll() {
      return lastRuntime().add(scheduleInterval).isLessThan(TimeValue.now());
   }
   
   @Override
   public void taskScheduled(TaskFuture scheduleFuture) {
      scheduleFuture.onTaskCompletion(new Runnable() {
         @Override
         public void run() {
            scheduled();
         }
      });
   }
}
