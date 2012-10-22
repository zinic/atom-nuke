package org.atomnuke.task.manager.impl;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.atomnuke.task.ManagedTask;
import org.atomnuke.task.manager.TaskTracker;
import org.atomnuke.util.remote.CancellationRemote;

/**
 *
 * @author zinic
 */
public class ThreadSafeTaskTracker implements TaskTracker {

   private final CancellationRemote cancellationRemote;
   private final List<ManagedTask> pollingTasks;

   public ThreadSafeTaskTracker(CancellationRemote cancellationRemote) {
      this.cancellationRemote = cancellationRemote;

      pollingTasks = new LinkedList<ManagedTask>();
   }

   @Override
   public boolean active() {
      return !cancellationRemote.canceled();
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
   public synchronized void add(ManagedTask task) {
      if (active()) {
         pollingTasks.add(task);
      } else {
         throw new IllegalStateException("This object has been destroyed and can no longer enlist tasks.");
      }
   }
}
