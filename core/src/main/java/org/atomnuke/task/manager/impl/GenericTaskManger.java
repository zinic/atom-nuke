package org.atomnuke.task.manager.impl;

import org.atomnuke.task.manager.TaskTracker;
import java.util.concurrent.TimeUnit;
import org.atomnuke.task.ManagedTask;
import org.atomnuke.task.manager.TaskManager;
import org.atomnuke.task.threading.ExecutionManager;
import org.atomnuke.util.TimeValue;
import org.atomnuke.util.remote.AtomicCancellationRemote;
import org.atomnuke.util.remote.CancellationRemote;

/**
 *
 * @author zinic
 */
public class GenericTaskManger implements TaskManager {

   private static final TimeValue THREE_MILLISECONDS = new TimeValue(3, TimeUnit.MILLISECONDS);

   private final CancellationRemote cancellationRemote;
   private final ExecutionManager executionManager;
   private final TaskTracker taskTracker;

   public GenericTaskManger(ExecutionManager executionManager) {
      this.cancellationRemote = new AtomicCancellationRemote();
      this.executionManager = executionManager;

      this.taskTracker = new TaskTrackerImpl(cancellationRemote);
   }

   @Override
   public synchronized void destroy() {
      cancellationRemote.cancel();
      executionManager.destroy();

      // Cancel all of the executing tasks
      for (ManagedTask managedTask : taskTracker.activeTasks()) {
         managedTask.handle().cancellationRemote().cancel();
      }
   }

   @Override
   public TaskTracker tracker() {
      return taskTracker;
   }

   @Override
   public TimeValue scheduleTasks() {
      final TimeValue now = TimeValue.now();
      TimeValue closestPollTime = now.add(THREE_MILLISECONDS);

      for (ManagedTask managedTask : taskTracker.activeTasks()) {
         TimeValue nextPollTime = managedTask.nextPollTime();

         // Sould this task be scheduled? If so, is the task already in the execution queue?
         if (now.isGreaterThan(nextPollTime) && !executionManager.submitted(managedTask.handle())) {
            executionManager.submit(managedTask.handle(), managedTask);
            managedTask.scheduleNext();

            nextPollTime = managedTask.nextPollTime();
         }

         if (closestPollTime.isGreaterThan(nextPollTime)) {
            // If the closest polling time is null or later than this task's
            // next polling time, it becomes the next time the kernel wakes
            closestPollTime = nextPollTime;
         }
      }

      return closestPollTime;
   }
}