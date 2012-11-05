package org.atomnuke.task.manager.impl;

import java.util.concurrent.TimeUnit;
import org.atomnuke.task.ManagedTask;
import org.atomnuke.task.TaskHandle;
import org.atomnuke.task.manager.TaskManager;
import org.atomnuke.task.manager.TaskTracker;
import org.atomnuke.task.threading.ExecutionManager;
import org.atomnuke.util.TimeValue;

/**
 *
 * @author zinic
 */
public class GenericTaskManger implements TaskManager {

   private static final TimeValue DEFAULT_SLEEP_INTERVAL = new TimeValue(3, TimeUnit.MILLISECONDS);

   private final ExecutionManager executionManager;
   private final TaskTracker taskTracker;

   public GenericTaskManger(ExecutionManager executionManager, TaskTracker taskTracker) {
      this.executionManager = executionManager;
      this.taskTracker = taskTracker;
   }

   @Override
   public State state() {
      if (!taskTracker.active()) {
         return State.DESTROYED;
      }

      switch(executionManager.state()) {
         case NEW:
         case STARTING:
            return State.NEW;

         case STOPPING:
         case DESTROYED:
            return State.DESTROYED;
      }

      return State.READY;
   }

   @Override
   public ManagedTask findTask(long taskId) {
      for (ManagedTask managedTask : taskTracker.activeTasks()) {
         if (managedTask.handle().id() == taskId) {
            return managedTask;
         }
      }

      return null;
   }

   @Override
   public void destroy() {
      // Cancel all of the executing tasks
      for (ManagedTask managedTask : taskTracker.activeTasks()) {
         managedTask.handle().cancellationRemote().cancel();
      }
   }

   @Override
   public TimeValue scheduleTasks() {
      final TimeValue now = TimeValue.now();
      TimeValue closestPollTime = null;

      for (ManagedTask managedTask : taskTracker.activeTasks()) {
         final TaskHandle taskHandle = managedTask.handle();
         TimeValue nextPollTime = managedTask.nextRunTime();

         // Sould this task be scheduled? If so, is the task already in the execution queue?
         if (now.isGreaterThan(nextPollTime) && (taskHandle.reenterant() || !executionManager.submitted(taskHandle.id()))) {
            executionManager.submit(taskHandle.id(), managedTask);
            managedTask.scheduleNext();

            nextPollTime = managedTask.nextRunTime();
         }

         if (closestPollTime == null || closestPollTime.isGreaterThan(nextPollTime)) {
            // If the closest polling time is null or later than this task's
            // next polling time, it becomes the next time the kernel wakes
            closestPollTime = nextPollTime;
         }
      }

      return closestPollTime == null ? now.add(DEFAULT_SLEEP_INTERVAL) : closestPollTime;
   }
}