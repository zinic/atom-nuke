package org.atomnuke.task.polling;

import org.atomnuke.task.TaskHandle;
import org.atomnuke.util.TimeValue;

/**
 *
 * @author zinic
 */
public class TimeIntervalPollingController extends AbstractPollingController {
   
   private final TimeValue scheduleInterval;
   private TimeValue nextRun;
   
   public TimeIntervalPollingController(TaskHandle taskHandle, TimeValue scheduleInterval) {
      super(taskHandle);
      
      this.scheduleInterval = scheduleInterval;
      
      scheduled();
   }
   
   private synchronized void scheduled() {
      nextRun = TimeValue.now().add(scheduleInterval);
   }
   
   @Override
   public boolean shouldPoll(TimeValue now) {
      return nextRun.isLessThan(now);
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
