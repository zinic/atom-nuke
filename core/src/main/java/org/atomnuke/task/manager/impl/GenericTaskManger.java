package org.atomnuke.task.manager.impl;

import java.util.concurrent.TimeUnit;
import org.atomnuke.task.ManagedTask;
import org.atomnuke.task.TaskHandle;
import org.atomnuke.task.manager.TaskManager;
import org.atomnuke.task.manager.TaskTracker;
import org.atomnuke.task.polling.PollingController;
import org.atomnuke.task.polling.TaskFutureImpl;
import org.atomnuke.task.threading.ExecutionLifeCycle;
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

      for (ManagedTask managedTask : taskTracker.activeTasks()) {
         final PollingController pollingController = managedTask.pollingController();
         final TaskHandle taskHandle = managedTask.handle();

         // Sould this task be scheduled? If so, is the task already in the execution queue?
         if (pollingController.shouldPoll() && (taskHandle.reenterant() || !executionManager.submitted(taskHandle.id()))) {
            // Create a new future for this scheduling pass of the task - this is used to communicate progress
            final TaskFutureImpl taskFuture = new TaskFutureImpl();
            
            // Tell the polling controller that we're being tasked before actually submitting the task
            pollingController.taskScheduled(taskFuture);
            
            // Submit the runnable representation of the task
            executionManager.submit(taskHandle.id(), new ExecutionLifeCycle(managedTask, taskFuture));
         }
      }

      return taskTracker.shouldSchedule() ? now : now.add(DEFAULT_SLEEP_INTERVAL);
   }
}