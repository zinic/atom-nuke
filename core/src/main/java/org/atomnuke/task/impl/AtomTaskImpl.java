package org.atomnuke.task.impl;

import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.plugin.env.NopInstanceEnvironment;
import org.atomnuke.listener.AtomListener;
import org.atomnuke.listener.manager.ListenerManager;
import org.atomnuke.plugin.InstanceContextImpl;
import org.atomnuke.task.AtomTask;
import org.atomnuke.task.TaskHandle;
import org.atomnuke.util.remote.CancellationRemote;

/**
 *
 * @author zinic
 */
public class AtomTaskImpl implements AtomTask {

   private final ListenerManager listenerManager;
   private final TaskHandle taskHandle;

   public AtomTaskImpl(ListenerManager listenerManager, TaskHandle taskHandle) {
      this.listenerManager = listenerManager;
      this.taskHandle = taskHandle;
   }

   @Override
   public TaskHandle handle() {
      return taskHandle;
   }

   @Override
   public CancellationRemote addListener(AtomListener listener) {
      return addListener(new InstanceContextImpl<AtomListener>(NopInstanceEnvironment.getInstance(), listener));
   }

   @Override
   public CancellationRemote addListener(InstanceContext<? extends AtomListener> atomListenerContext) {
      return listenerManager.addListener(atomListenerContext);
   }
}
