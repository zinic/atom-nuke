package net.jps.nuke.task;

import net.jps.nuke.listener.AtomListener;
import net.jps.nuke.listener.manager.ListenerManager;
import net.jps.nuke.task.context.TaskContext;
import net.jps.nuke.task.lifecycle.InitializationException;
import net.jps.nuke.util.TimeValue;
import net.jps.nuke.util.remote.AtomicCancellationRemote;
import net.jps.nuke.util.remote.CancellationRemote;

/**
 *
 * @author zinic
 */
public class TaskImpl implements Task {

   private final CancellationRemote cancelationRemote;
   private final ListenerManager listenerManager;
   private final TaskContext context;
   private final TimeValue interval;

   public TaskImpl(TaskContext context, TimeValue interval, ListenerManager listenerManager) {
      this.cancelationRemote = new AtomicCancellationRemote();
      this.listenerManager = listenerManager;
      this.context = context;
      this.interval = interval;
   }

   @Override
   public boolean canceled() {
      return cancelationRemote.canceled();
   }

   @Override
   public void cancel() {
      cancelationRemote.cancel();
   }

   @Override
   public TimeValue interval() {
      return interval;
   }

   @Override
   public void addListener(AtomListener listener) throws InitializationException {
      listener.init(context);
      
      listenerManager.addListener(listener);
   }
}
