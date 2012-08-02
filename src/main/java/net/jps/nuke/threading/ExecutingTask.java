package net.jps.nuke.threading;

import java.util.concurrent.Future;
import net.jps.nuke.task.Task;
import net.jps.nuke.task.lifecycle.TaskLifeCycle;

/**
 *
 * @author zinic
 */
public class ExecutingTask {

   private final Future future;
   private final Task task;

   public ExecutingTask(Future future, Task task) {
      this.future = future;
      this.task = task;
   }

   public TaskLifeCycle taskLifeCycle() {
      return task;
   }

   public void cancel() {
      task.cancel();
   }

   public boolean done() {
      return future.isDone();
   }
}
