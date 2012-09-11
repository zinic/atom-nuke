package org.atomnuke.task;

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
      addListenerContext(new SimpleInstanceContext<AtomListener>(listener));
   }

   @Override
   public void addListenerContext(InstanceContext<? extends AtomListener> listenerContext) throws InitializationException {
      listenerContext.stepInto();

      try {
         listenerContext.getInstance().init(context);
         listenerManager.addListener(listenerContext);
      } finally {
         listenerContext.stepOut();
      }
   }
}
