package org.atomnuke.task.threading;

import java.util.concurrent.Future;

/**
 *
 * @author zinic
 */
public class TrackedFuture {

   private final Future future;
   private final long taskId;

   public TrackedFuture(Future future, long taskId) {
      this.future = future;
      this.taskId = taskId;
   }

   public Future future() {
      return future;
   }

   public long taskId() {
      return taskId;
   }

   public boolean done() {
      return future.isDone();
   }
}
