package org.atomnuke.task;

import java.util.UUID;
import org.atomnuke.context.InstanceContext;
import org.atomnuke.context.SimpleInstanceContext;
import org.atomnuke.listener.AtomListener;
import org.atomnuke.listener.manager.ListenerManager;
import org.atomnuke.task.context.TaskContext;
import org.atomnuke.task.lifecycle.InitializationException;
import org.atomnuke.util.TimeValue;
import org.atomnuke.util.remote.AtomicCancellationRemote;
import org.atomnuke.util.remote.CancellationRemote;

/**
 *
 * @author zinic
 */
public class TaskImpl implements Task {

   private final CancellationRemote cancelationRemote;
   private final ListenerManager listenerManager;
   private final TaskContext context;
   private final TimeValue interval;
   private final UUID taskId;

   public TaskImpl(UUID taskId, ListenerManager listenerManager, TaskContext context, TimeValue interval) {
      this.listenerManager = listenerManager;
      this.context = context;
      this.interval = interval;
      this.taskId = taskId;

      this.cancelationRemote = new AtomicCancellationRemote();
   }

   @Override
   public UUID id() {
      return taskId;
   }

   @Override
   public CancellationRemote cancellationRemote() {
      return cancelationRemote;
   }

   @Override
   public TimeValue interval() {
      return interval;
   }

   @Override
   public CancellationRemote addListener(AtomListener listener) throws InitializationException {
      return addListener(new SimpleInstanceContext<AtomListener>(listener));
   }

   @Override
   public CancellationRemote addListener(InstanceContext<? extends AtomListener> listenerContext) throws InitializationException {
      listenerContext.stepInto();

      try {
         listenerContext.getInstance().init(context);
         return listenerManager.addListener(listenerContext);
      } finally {
         listenerContext.stepOut();
      }
   }
}
