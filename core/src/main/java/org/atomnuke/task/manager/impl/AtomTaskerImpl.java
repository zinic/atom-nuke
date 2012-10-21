package org.atomnuke.task.manager.impl;

import org.atomnuke.task.impl.ManagedAtomTask;
import org.atomnuke.task.manager.Tasker;
import java.util.UUID;
import org.atomnuke.listener.manager.ListenerManager;
import org.atomnuke.listener.manager.ListenerManagerImpl;
import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.service.gc.ReclaimationHandler;
import org.atomnuke.source.AtomSource;
import org.atomnuke.task.AtomTask;
import org.atomnuke.task.TaskHandle;
import org.atomnuke.task.impl.AtomTaskImpl;
import org.atomnuke.task.impl.TaskHandleImpl;
import org.atomnuke.task.manager.AtomTasker;
import org.atomnuke.task.threading.ExecutionManager;
import org.atomnuke.util.TimeValue;
import org.atomnuke.util.remote.CancellationRemote;

/**
 *
 * @author zinic
 */
public class AtomTaskerImpl implements AtomTasker {

   private final ReclaimationHandler reclaimationHandler;
   private final ExecutionManager executionManager;
   private final Tasker tasker;

   public AtomTaskerImpl(ReclaimationHandler reclaimationHandler, ExecutionManager executionManager, Tasker tasker) {
      this.reclaimationHandler = reclaimationHandler;
      this.executionManager = executionManager;
      this.tasker = tasker;
   }

   @Override
   public AtomTask follow(InstanceContext<AtomSource> source, TimeValue pollingInterval) {
      final CancellationRemote cancellationRemote = reclaimationHandler.watch(source);

      // Generate a new UUID for the polling task we're about to register
      final TaskHandle newTaskHandle = new TaskHandleImpl(cancellationRemote, pollingInterval, UUID.randomUUID());
      final ListenerManager listenerManager = new ListenerManagerImpl(reclaimationHandler, newTaskHandle);

      // Register and track it.
      final ManagedAtomTask managedAtomTask = new ManagedAtomTask(source, executionManager, listenerManager, newTaskHandle);
      tasker.task(managedAtomTask);

      return new AtomTaskImpl(listenerManager, newTaskHandle);
   }
}
