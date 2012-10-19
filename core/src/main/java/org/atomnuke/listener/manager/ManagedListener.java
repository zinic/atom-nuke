package org.atomnuke.listener.manager;

import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.listener.AtomListener;
import org.atomnuke.task.TaskHandle;
import org.atomnuke.util.remote.AtomicCancellationRemote;
import org.atomnuke.util.remote.CancellationRemote;

/**
 *
 * @author zinic
 */
public class ManagedListener {

   private final InstanceContext<AtomListener> listenerContext;
   private final CancellationRemote cancellationRemote;
   private final TaskHandle parentHandle;

   public ManagedListener(InstanceContext<AtomListener> listenerContext, TaskHandle parentHandle) {
      this.listenerContext = listenerContext;
      this.parentHandle = parentHandle;

      cancellationRemote = new AtomicCancellationRemote();
   }

   public CancellationRemote cancellationRemote() {
      return cancellationRemote;
   }

   public TaskHandle parentHandle() {
      return parentHandle;
   }

   public InstanceContext<AtomListener> listenerContext() {
      return listenerContext;
   }
}
