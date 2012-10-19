package org.atomnuke.task.manager.impl;

import org.atomnuke.task.manager.TaskTracker;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.atomnuke.task.manager.ManagedTask;
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
      final List<ManagedTask> activeTasks = new LinkedList<ManagedTask>();

      for (Iterator<ManagedTask> managedTaskIter = pollingTasks.iterator(); managedTaskIter.hasNext();) {
         final ManagedTask managedTask = managedTaskIter.next();

         // Cancellation of a task may occur at anytime
         if (!managedTask.cancellationRemote().canceled()) {
            activeTasks.add(managedTask);
         } else {
            managedTaskIter.remove();
         }
      }

      return activeTasks;
   }

   @Override
   public synchronized void addTask(ManagedTask managedTask) {
      if (cancellationRemote.canceled()) {
         // TODO:Implement - Consider returning a boolean value to communicate shutdown?
         LOG.warn("This object has been destroyed and can no longer enlist tasks.");
         return;
      }
   }
}
