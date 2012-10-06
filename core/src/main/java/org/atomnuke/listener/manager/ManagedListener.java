package org.atomnuke.listener.manager;

import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.listener.AtomListener;
import org.atomnuke.task.context.TaskContext;
import org.atomnuke.task.lifecycle.InitializationException;
import org.atomnuke.util.remote.CancellationRemote;

/**
 *
 * @author zinic
 */
public class ManagedListener {

   private final InstanceContext<? extends AtomListener> listenerContext;
   private final CancellationRemote cancellationRemote;

   public ManagedListener(CancellationRemote cancellationRemote, InstanceContext<? extends AtomListener> listenerContext) {
      this.cancellationRemote = cancellationRemote;
      this.listenerContext = listenerContext;
   }

   public CancellationRemote cancellationRemote() {
      return cancellationRemote;
   }

   public void init(TaskContext tc) throws InitializationException {
      try {
         listenerContext.environment().stepInto();
         listenerContext.instance().init(tc);
      } finally {
         listenerContext.environment().stepOut();
      }
   }

   public void cancel() {
      cancellationRemote.cancel();
   }

   public InstanceContext<? extends AtomListener> listenerContext() {
      return listenerContext;
   }
}
