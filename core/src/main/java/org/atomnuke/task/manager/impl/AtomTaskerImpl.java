package org.atomnuke.task.manager.impl;

import org.atomnuke.task.impl.ManagedAtomTask;
import org.atomnuke.task.manager.TaskTracker;
import java.util.UUID;
import org.atomnuke.listener.manager.ListenerManager;
import org.atomnuke.listener.manager.ListenerManagerImpl;
import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.source.AtomSource;
import org.atomnuke.task.AtomTask;
import org.atomnuke.task.TaskHandle;
import org.atomnuke.task.impl.AtomTaskImpl;
import org.atomnuke.task.impl.TaskHandleImpl;
import org.atomnuke.task.manager.AtomTasker;
import org.atomnuke.task.threading.ExecutionManager;
import org.atomnuke.util.TimeValue;

/**
 *
 * @author zinic
 */
public class AtomTaskerImpl implements AtomTasker {

   private final ExecutionManager executionManager;
   private final TaskTracker taskTracker;

   public AtomTaskerImpl(ExecutionManager executionManager, TaskTracker taskTracker) {
      this.executionManager = executionManager;
      this.taskTracker = taskTracker;
   }

   @Override
   public AtomTask follow(InstanceContext<AtomSource> source, TimeValue pollingInterval) {
      // Generate a new UUID for the polling task we're about to register
      final TaskHandle newTaskHandle = new TaskHandleImpl(UUID.randomUUID(), pollingInterval);
      final ListenerManager listenerManager = new ListenerManagerImpl(newTaskHandle);

      // Register and track it.
      final ManagedAtomTask managedAtomTask = new ManagedAtomTask(newTaskHandle, source, executionManager, listenerManager);
      taskTracker.addTask(managedAtomTask);

      return new AtomTaskImpl(listenerManager, newTaskHandle);
   }
}
