package org.atomnuke.task.manager.impl;

import org.atomnuke.task.impl.ManagedAtomTask;
import org.atomnuke.task.manager.Tasker;
import org.atomnuke.listener.manager.ListenerManager;
import org.atomnuke.listener.manager.ListenerManagerImpl;
import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.service.gc.ReclamationHandler;
import org.atomnuke.source.AtomSource;
import org.atomnuke.task.AtomTask;
import org.atomnuke.task.TaskHandle;
import org.atomnuke.task.impl.AtomTaskImpl;
import org.atomnuke.task.manager.AtomTasker;
import org.atomnuke.task.threading.ExecutionManager;
import org.atomnuke.util.TimeValue;

/**
 *
 * @author zinic
 */
public class AtomTaskerImpl implements AtomTasker {

   private final ReclamationHandler reclamationHandler;
   private final ExecutionManager executionManager;
   private final Tasker tasker;

   public AtomTaskerImpl(ReclamationHandler reclamationHandler, ExecutionManager executionManager, Tasker tasker) {
      this.reclamationHandler = reclamationHandler;
      this.executionManager = executionManager;
      this.tasker = tasker;
   }

   @Override
   public AtomTask follow(InstanceContext<AtomSource> source, TimeValue pollingInterval) {
      // New listener manager
      final ListenerManager listenerManager = new ListenerManagerImpl(reclamationHandler);

      // Register and track the new source
      final ManagedAtomTask managedAtomTask = new ManagedAtomTask(source, executionManager, listenerManager);
      final TaskHandle newHandle = tasker.task(managedAtomTask, pollingInterval);

      return new AtomTaskImpl(listenerManager, newHandle);
   }
}
