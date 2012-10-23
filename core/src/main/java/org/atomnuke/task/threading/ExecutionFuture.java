package org.atomnuke.task.threading;

import java.util.UUID;
import java.util.concurrent.Future;

/**
 *
 * @author zinic
 */
public class ExecutionFuture {

   private final Future future;
   private final UUID taskId;

   public ExecutionFuture(Future future, UUID taskId) {
      this.future = future;
      this.taskId = taskId;
   }

   public Future future() {
      return future;
   }

   public UUID taskId() {
      return taskId;
   }

   public boolean done() {
      return future.isDone();
   }
}
