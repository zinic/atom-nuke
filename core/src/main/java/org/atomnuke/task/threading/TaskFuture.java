package org.atomnuke.task.threading;

import java.util.concurrent.Future;
import org.atomnuke.task.TaskHandle;

/**
 *
 * @author zinic
 */
public class TaskFuture {

   private final TaskHandle taskHandle;
   private final Future future;

   public TaskFuture(TaskHandle taskHandle, Future future) {
      this.taskHandle = taskHandle;
      this.future = future;
   }

   public TaskHandle taskHandle() {
      return taskHandle;
   }

   public boolean done() {
      return future.isDone();
   }
}
