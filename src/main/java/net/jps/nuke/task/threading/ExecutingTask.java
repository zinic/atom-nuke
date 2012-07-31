package net.jps.nuke.task.threading;

import java.util.concurrent.Future;
import net.jps.nuke.task.ManagedTask;

/**
 *
 * @author zinic
 */
public class ExecutingTask {

   private final ManagedTask task;
   private final Future future;

   public ExecutingTask(ManagedTask task, Future future) {
      this.task = task;
      this.future = future;
   }

   public ManagedTask managedTask() {
      return task;
   }

   public void cancel() {
      task.cancel();
   }

   public void destroy() {
      task.destroy();
   }

   public boolean done() {
      return future.isDone();
   }
}
