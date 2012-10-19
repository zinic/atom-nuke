package org.atomnuke.task.manager.impl;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.atomnuke.task.manager.ManagedTask;
import org.atomnuke.task.manager.TaskManager;
import org.atomnuke.task.threading.ExecutionManager;
import org.atomnuke.util.TimeValue;
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

   public GenericTaskManger(CancellationRemote cancellationRemote, ExecutionManager executionManager, TaskTracker taskTracker) {
      this.cancellationRemote = cancellationRemote;
      this.executionManager = executionManager;
      this.taskTracker = taskTracker;
   }

   @Override
   public void init() {
   }

   @Override
   public synchronized void destroy() {
      cancellationRemote.cancel();
      executionManager.destroy();

      // Cancel all of the executing tasks
      for (ManagedTask managedTask : taskTracker.activeTasks()) {
         managedTask.cancellationRemote().cancel();
      }
   }

   @Override
   public ManagedTask findTask(UUID taskId) {
      for (ManagedTask managedTask : taskTracker.activeTasks()) {
         if (managedTask.id().equals(taskId)) {
            return managedTask;
         }
      }

      return null;
   }

   @Override
   public TimeValue scheduleTasks() {
      final TimeValue now = TimeValue.now();
      TimeValue closestPollTime = now.add(THREE_MILLISECONDS);

      for (ManagedTask managedTask : taskTracker.activeTasks()) {
         TimeValue nextPollTime = managedTask.nextPollTime();

         // Sould this task be scheduled? If so, is the task already in the execution queue?
         if (now.isGreaterThan(nextPollTime) && !executionManager.submitted(managedTask.id())) {
            executionManager.submit(managedTask.id(), managedTask);
            managedTask.scheduled();

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