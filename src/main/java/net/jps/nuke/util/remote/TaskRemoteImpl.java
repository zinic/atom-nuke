package net.jps.nuke.util.remote;

/**
 *
 * @author zinic
 */
public class TaskRemoteImpl implements TaskRemote {

   private Runnable task;

   public TaskRemoteImpl() {
   }

   @Override
   public synchronized Runnable nextTask() {
      final Runnable ref = task;
      task = null;

      return ref;
   }

   @Override
   public synchronized void updateTask(Runnable task) {
      this.task = task;
   }
}
