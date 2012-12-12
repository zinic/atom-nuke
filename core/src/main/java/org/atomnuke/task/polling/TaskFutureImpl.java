package org.atomnuke.task.polling;

/**
 *
 * @author zinic
 */
public class TaskFutureImpl implements TaskFuture {

   private Runnable taskCompletionRunnable;
   private boolean taskComplete;

   public synchronized boolean isTaskComplete() {
      return taskComplete;
   }
   
   public synchronized void taskComplete() {
      taskComplete = true;
   }
   
   @Override
   public void onTaskCompletion(Runnable runnable) {
      taskCompletionRunnable = runnable;
      
      if (isTaskComplete()) {
         runTaskCompletion();
      }
   }

   public void runTaskCompletion() {
      if (taskCompletionRunnable != null) {
         taskCompletionRunnable.run();
      }
   }
}
