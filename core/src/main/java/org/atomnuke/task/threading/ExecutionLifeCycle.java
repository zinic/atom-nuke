package org.atomnuke.task.threading;

import org.atomnuke.task.polling.TaskFutureImpl;

/**
 *
 * @author zinic
 */
public class ExecutionLifeCycle implements Runnable {

   private final TaskFutureImpl scheduleFuture;
   private final Runnable runnable;

   public ExecutionLifeCycle(Runnable runnable, TaskFutureImpl scheduleFuture) {
      this.runnable = runnable;
      this.scheduleFuture = scheduleFuture;
   }

   @Override
   public void run() {
      try {
         runnable.run();
      } finally {
         scheduleFuture.runTaskCompletion();
      }
   }
}
