package net.jps.nuke.listener;

import net.jps.nuke.task.TaskContext;
import net.jps.nuke.util.remote.CancellationRemote;

/**
 *
 * @author zinic
 */
public class RegisteredListener {

   private final CancellationRemote cancellationRemote;
   private final TaskContext taskContext;
   private final AtomListener listener;

   public RegisteredListener(CancellationRemote cancellationRemote, TaskContext taskContext, AtomListener listener) {
      this.cancellationRemote = cancellationRemote;
      this.taskContext = taskContext;
      this.listener = listener;
   }

   public TaskContext taskContext() {
      return taskContext;
   }

   public CancellationRemote cancellationRemote() {
      return cancellationRemote;
   }

   public AtomListener listener() {
      return listener;
   }
}
