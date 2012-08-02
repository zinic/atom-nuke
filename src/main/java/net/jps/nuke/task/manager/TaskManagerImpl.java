package net.jps.nuke.task.manager;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import net.jps.nuke.source.AtomSource;
import net.jps.nuke.task.Task;
import net.jps.nuke.task.context.TaskContext;
import net.jps.nuke.task.context.TaskContextImpl;
import net.jps.nuke.task.threading.ExecutionManager;
import net.jps.nuke.util.TimeValue;
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

   public synchronized void addTask(ManagedTask state) {
      if (!allowSubmission) {
         throw new IllegalStateException("This object has been destroyed and can no longer enlist tasks.");
      }

      pollingTasks.add(state);
   }

   @Override
   public synchronized void destroy() {
      allowSubmission = false;

      executionManager.destroy();
      
      // Cancel all of the executing tasks
      for (ManagedTask task : pollingTasks) {
         task.cancel();
      }

      // Destroy the tasks
      for (ManagedTask task : pollingTasks) {
         task.destroy(taskContext);
      }
   }

   @Override
   public synchronized List<ManagedTask> tasks() {
      final List<ManagedTask> activeTasks = new LinkedList<ManagedTask>();

      for (Iterator<ManagedTask> managedTaskIter = pollingTasks.iterator(); managedTaskIter.hasNext();) {
         final ManagedTask potentiallyActiveTask = managedTaskIter.next();

         if (!potentiallyActiveTask.canceled()) {
            activeTasks.add(potentiallyActiveTask);
         } else {
            managedTaskIter.remove();
         }
      }

      return activeTasks;
   }

   @Override
   public TimeValue scheduleTasks() {
      final TimeValue now = TimeValue.now();
      TimeValue closestPollTime = null;

      for (ManagedTask managedTask : tasks()) {
         final TimeValue nextPollTime = managedTask.nextPollTime();

         // Sould this task be scheduled? If so, is the task already in the execution queue?
         if (now.isGreaterThan(nextPollTime)) {

            // Reentrant tasks are always eligible to run if their next polling
            // time has arrived.
            if (managedTask.isReentrant() || !executionManager.submitted(managedTask.id())) {
               executionManager.submit(managedTask.id(), managedTask);
            }
         } else if (closestPollTime == null || closestPollTime.isGreaterThan(nextPollTime)) {
            // If the closest polling time is null or later than this task's
            // next polling time, it becomes the next time the kernel wakes

            closestPollTime = nextPollTime;
         }
      }

      return closestPollTime;
   }

   @Override
   public Task follow(AtomSource source) {
      return follow(source, new TimeValue(1, TimeUnit.MINUTES));
   }

   @Override
   public Task follow(AtomSource source, TimeValue pollingInterval) {
      final ManagedTask managedTask = new ManagedTask(taskContext, pollingInterval, executionManager, source);
      addTask(managedTask);

      return managedTask;
   }
}
