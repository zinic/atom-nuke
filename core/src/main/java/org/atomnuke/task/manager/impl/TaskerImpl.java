package org.atomnuke.task.manager.impl;

import org.atomnuke.task.manager.Tasker;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import org.atomnuke.task.ManagedTask;
import org.atomnuke.task.TaskHandle;
import org.atomnuke.task.impl.ManagedRunTask;
import org.atomnuke.task.impl.TaskHandleImpl;
import org.atomnuke.util.TimeValue;
import org.atomnuke.util.remote.CancellationRemote;

/**
 *
 * @author zinic
 */
public class TaskerImpl implements Tasker {

   private final CancellationRemote cancellationRemote;
   private final List<ManagedTask> pollingTasks;

   public TaskerImpl(CancellationRemote cancellationRemote) {
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

   public void checkState() {
      if (cancellationRemote.canceled()) {
         throw new IllegalStateException("This object has been destroyed and can no longer enlist tasks.");
      }
   }

   @Override
   public TaskHandle task(Runnable runnable, TimeValue taskInterval) {
      final TaskHandle taskHandle = new TaskHandleImpl(cancellationRemote, taskInterval, UUID.randomUUID());
      task(new ManagedRunTask(runnable, taskHandle));

      return taskHandle;
   }

   @Override
   public synchronized void task(ManagedTask managedTask) {
      checkState();

      pollingTasks.add(managedTask);
   }
}
