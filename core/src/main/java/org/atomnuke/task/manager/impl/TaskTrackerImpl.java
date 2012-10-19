package org.atomnuke.task.manager.impl;

import org.atomnuke.task.manager.TaskTracker;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import org.atomnuke.task.ManagedTask;
import org.atomnuke.util.remote.CancellationRemote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class TaskTrackerImpl implements TaskTracker {

   private static final Logger LOG = LoggerFactory.getLogger(TaskTrackerImpl.class);

   private final CancellationRemote cancellationRemote;
   private final List<ManagedTask> pollingTasks;

   public TaskTrackerImpl(CancellationRemote cancellationRemote) {
      this.cancellationRemote = cancellationRemote;

      pollingTasks = new LinkedList<ManagedTask>();
   }

   @Override
   public synchronized List<ManagedTask> activeTasks() {
      for (Iterator<ManagedTask> managedTaskIter = pollingTasks.iterator(); managedTaskIter.hasNext();) {
         final ManagedTask managedTask = managedTaskIter.next();

         // Cancellation of a task may occur at anytime - if it was canceled, remove it
         if (managedTask.handle().cancellationRemote().canceled()) {
            managedTaskIter.remove();
         }
      }

      return new LinkedList<ManagedTask>(pollingTasks);
   }

   @Override
   public ManagedTask findTask(UUID taskId) {
      for (ManagedTask managedTask : activeTasks()) {
         if (managedTask.handle().id().equals(taskId)) {
            return managedTask;
         }
      }

      return null;
   }

   @Override
   public synchronized void registerTask(ManagedTask managedTask) {
      if (cancellationRemote.canceled()) {
         // TODO:Implement - Consider returning a boolean value to communicate shutdown?
         LOG.warn("This object has been destroyed and can no longer enlist tasks.");
         return;
      }

      pollingTasks.add(managedTask);
   }
}
