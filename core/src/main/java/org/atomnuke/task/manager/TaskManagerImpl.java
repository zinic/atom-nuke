package org.atomnuke.task.manager;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import org.atomnuke.context.InstanceContext;
import org.atomnuke.context.SimpleInstanceContext;
import org.atomnuke.listener.manager.ListenerManager;
import org.atomnuke.listener.manager.ListenerManagerImpl;
import org.atomnuke.source.AtomSource;
import org.atomnuke.task.Task;
import org.atomnuke.task.TaskImpl;
import org.atomnuke.task.context.TaskContext;
import org.atomnuke.task.context.TaskContextImpl;
import org.atomnuke.task.lifecycle.InitializationException;
import org.atomnuke.task.threading.ExecutionManager;
import org.atomnuke.util.TimeValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class TaskManagerImpl implements TaskManager {

   private static final Logger LOG = LoggerFactory.getLogger(TaskManagerImpl.class);
   private final ExecutionManager executionManager;
   private final List<ManagedTask> pollingTasks;
   private final TaskContext taskContext;
   private boolean allowSubmission;

   public TaskManagerImpl(ExecutionManager executionManager) {
      this.executionManager = executionManager;
      this.taskContext = new TaskContextImpl(this);

      pollingTasks = new LinkedList<ManagedTask>();
      allowSubmission = true;
   }

   @Override
   public void init() {
   }

   @Override
   public synchronized void destroy() {
      allowSubmission = false;

      executionManager.destroy();

      // Cancel all of the executing tasks
      for (ManagedTask managedTask : pollingTasks) {
         managedTask.cancel();
      }

      // Destroy the tasks
      for (ManagedTask managedTask : pollingTasks) {
         try {
            managedTask.destroy(taskContext);
         } catch (Exception ex) {
            LOG.error("Failed to destroy task: " + managedTask.id().toString() + ". Reason: " + ex.getMessage(), ex);
         }
      }
   }

   private synchronized void addTask(ManagedTask state) {
      if (!allowSubmission) {
         // TODO:Implement - Consider returning a boolean value to communicate shutdown?
         LOG.warn("This object has been destroyed and can no longer enlist tasks.");
         return;
      }

      pollingTasks.add(state);
   }

   private synchronized List<ManagedTask> activeTasks() {
      final List<ManagedTask> activeTasks = new LinkedList<ManagedTask>();
      final List<ManagedTask> destroyableTasks = new LinkedList<ManagedTask>();

      for (Iterator<ManagedTask> managedTaskIter = pollingTasks.iterator(); managedTaskIter.hasNext();) {
         final ManagedTask managedTask = managedTaskIter.next();

         // Cancellation of a task may occur at anytime
         if (!managedTask.canceled()) {
            activeTasks.add(managedTask);
         } else {
            // If the task has been canceled, schedule it for destruction
            destroyableTasks.add(managedTask);
            managedTaskIter.remove();
         }
      }

      for (ManagedTask managedTask : destroyableTasks) {
         try {
            managedTask.destroy(taskContext);
         } catch (Exception ex) {
            LOG.error("Failed to destroy task: " + managedTask.id().toString() + ". Reason: " + ex.getMessage(), ex);
         }
      }

      return activeTasks;
   }

   @Override
   public ManagedTask findTask(UUID taskId) {
      for (ManagedTask managedTask : activeTasks()) {
         if (managedTask.id().equals(taskId)) {
            return managedTask;
         }
      }

      return null;
   }

   @Override
   public TimeValue scheduleTasks() {
      final TimeValue now = TimeValue.now();
      TimeValue closestPollTime = null;

      for (ManagedTask managedTask : activeTasks()) {
         final TimeValue nextPollTime = managedTask.nextPollTime();

         // Sould this task be scheduled? If so, is the task already in the execution queue?
         if (now.isGreaterThan(nextPollTime) && !executionManager.submitted(managedTask.id())) {
            executionManager.submit(managedTask.id(), managedTask);
            managedTask.scheduled();
         } else if (closestPollTime == null || closestPollTime.isGreaterThan(nextPollTime)) {
            // If the closest polling time is null or later than this task's
            // next polling time, it becomes the next time the kernel wakes
            closestPollTime = nextPollTime;
         }
      }

      return closestPollTime != null ? closestPollTime : TimeValue.zero();
   }

   @Override
   public Task follow(InstanceContext<AtomSource> source, TimeValue pollingInterval) throws InitializationException {
      final ListenerManager listenerManager = new ListenerManagerImpl();

      final UUID taskId = UUID.randomUUID();
      final Task task = new TaskImpl(taskId, listenerManager, taskContext, pollingInterval);
      final ManagedTaskImpl managedTask = new ManagedTaskImpl(task, listenerManager, pollingInterval, executionManager, source);

      managedTask.init(taskContext);
      addTask(managedTask);

      return task;
   }
}
