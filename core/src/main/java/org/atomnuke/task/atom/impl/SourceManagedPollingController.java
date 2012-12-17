package org.atomnuke.task.atom.impl;

import org.atomnuke.task.polling.PollingController;
import org.atomnuke.task.polling.TaskFuture;
import org.atomnuke.util.TimeValue;

/**
 *
 * @author zinic
 */
public class SourceManagedPollingController implements PollingController {

   private final TimeValue scheduleInterval;
   
   private TimeValue nextRun;
   private boolean waitForSchedule;

   public SourceManagedPollingController(TimeValue scheduleInterval) {
      waitForSchedule = true;
      nextRun = TimeValue.now().add(scheduleInterval);
      
      this.scheduleInterval = scheduleInterval;
   }

   public synchronized void honorSchedule() {
      waitForSchedule = true;
   }
   
   public synchronized void ignoreSchedule() {
      waitForSchedule = false;
   }
      
   private synchronized void runCompleted() {
      nextRun = TimeValue.now().add(scheduleInterval);
   }

   @Override
   public synchronized boolean shouldPoll(TimeValue now) {
      return !waitForSchedule || nextRun.isLessThan(now);
   }

   @Override
   public synchronized void taskScheduled(TaskFuture scheduleFuture) {
      scheduleFuture.onTaskCompletion(new Runnable() {
         @Override
         public void run() {
            runCompleted();
         }
      });
   }
}
