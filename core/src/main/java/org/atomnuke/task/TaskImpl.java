package org.atomnuke.task;

import java.util.UUID;
import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.plugin.local.LocalInstanceEnvironment;
import org.atomnuke.listener.AtomListener;
import org.atomnuke.listener.manager.ListenerManager;
import org.atomnuke.plugin.InstanceContextImpl;
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
   private final TimeValue interval;
   private final UUID taskId;

   public TaskImpl(UUID taskId, ListenerManager listenerManager, TimeValue interval) {
      this.listenerManager = listenerManager;
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
   public CancellationRemote addListener(AtomListener listener) {
      return addListener(new InstanceContextImpl<AtomListener>(LocalInstanceEnvironment.getInstance(), listener));
   }

   @Override
   public CancellationRemote addListener(InstanceContext<? extends AtomListener> atomListenerContext) {
      return listenerManager.addListener(atomListenerContext);
   }
}
