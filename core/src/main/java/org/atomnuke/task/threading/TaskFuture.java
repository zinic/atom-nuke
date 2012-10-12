package org.atomnuke.task.threading;

import java.util.UUID;
import java.util.concurrent.Future;

/**
 *
 * @author zinic
 */
public class TaskFuture {

   private final Future future;
   private final UUID id;

   public TaskFuture(Future future, UUID id) {
      this.future = future;
      this.id = id;
   }

   public UUID id() {
      return id;
   }

   public boolean done() {
      return future. isDone();
   }
}
