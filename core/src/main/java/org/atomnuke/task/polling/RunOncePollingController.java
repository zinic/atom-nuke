package org.atomnuke.task.polling;

import org.atomnuke.task.TaskHandle;
import org.atomnuke.util.TimeValue;

/**
 *
 * @author zinic
 */
public class RunOncePollingController extends AbstractPollingController {

   private boolean scheduled;

   public RunOncePollingController(TaskHandle taskHandle) {
      super(taskHandle);

      scheduled = false;
   }

   @Override
   public synchronized void taskScheduled(TaskFuture scheduleFuture) {
      scheduled = true;
      
      scheduleFuture.onTaskCompletion(new Runnable() {
         @Override
         public void run() {
            // After scheduling, we ask to be reclaimed
            taskHandle().cancellationRemote().cancel();
         }
      });
   }

   @Override
   public synchronized boolean shouldPoll() {
      return !scheduled;
   }
}
