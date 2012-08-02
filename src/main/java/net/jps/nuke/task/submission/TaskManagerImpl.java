package net.jps.nuke.task.submission;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import net.jps.nuke.source.AtomSource;
import net.jps.nuke.task.ManagedTask;
import net.jps.nuke.task.ManagedTaskImpl;
import net.jps.nuke.task.Task;
import net.jps.nuke.task.context.TaskContext;
import net.jps.nuke.task.context.TaskContextImpl;
import net.jps.nuke.task.lifecycle.DestructionException;
import net.jps.nuke.threading.ExecutionManager;
import net.jps.nuke.util.TimeValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class TaskManagerImpl implements TaskManager {

   private static final Logger LOG = LoggerFactory.getLogger(TaskManagerImpl.class);
   
   private final ExecutionManager executorService;
   private final List<ManagedTask> pollingTasks;
   private final TaskContext taskContext;
   private boolean allowSubmission;

   public TaskManagerImpl(ExecutionManager executorService) {
      this.executorService = executorService;
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

      // Cancel all of the executing tasks
      for (ManagedTask task : pollingTasks) {
         task.cancel();
      }

      // Destroy the tasks
      for (ManagedTask task : pollingTasks) {
         try {
            task.destroy(taskContext);
         } catch (DestructionException de) {
            LOG.error(de.getMessage(), de);
         }
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
   public Task follow(AtomSource source) {
      return follow(source, new TimeValue(1, TimeUnit.MINUTES));
   }

   @Override
   public Task follow(AtomSource source, TimeValue pollingInterval) {
      final ManagedTaskImpl managedTask = new ManagedTaskImpl(taskContext, pollingInterval, executorService, source);
      addTask(managedTask);

      return managedTask;
   }
}
