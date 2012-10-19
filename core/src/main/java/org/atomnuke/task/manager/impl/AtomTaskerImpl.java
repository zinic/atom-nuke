package org.atomnuke.task.manager.impl;

import org.atomnuke.task.manager.TaskTracker;
import java.util.UUID;
import org.atomnuke.listener.manager.ListenerManager;
import org.atomnuke.listener.manager.ListenerManagerImpl;
import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.source.AtomSource;
import org.atomnuke.task.AtomTask;
import org.atomnuke.task.AtomTaskImpl;
import org.atomnuke.task.AtomTasker;
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
      final ListenerManager listenerManager = new ListenerManagerImpl();

      final UUID taskId = UUID.randomUUID();
      final AtomTask task = new AtomTaskImpl(taskId, listenerManager, pollingInterval);

      final ManagedTaskImpl managedTask = new ManagedTaskImpl(task, listenerManager, pollingInterval, executionManager, source);
      taskTracker.addTask(managedTask);

      return task;
   }
}
