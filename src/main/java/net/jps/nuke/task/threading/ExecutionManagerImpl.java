package net.jps.nuke.task.threading;

import net.jps.nuke.task.ManagedTask;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 *
 * @author zinic
 */
public class ExecutionManagerImpl implements ExecutionManager {

   private final Map<ManagedTask, Future> executionStates;
   private final ExecutorService executorService;

   public ExecutionManagerImpl(ExecutorService executorService) {
      this.executorService = executorService;
      executionStates = new HashMap<ManagedTask, Future>();
   }

   @Override
   public synchronized void destroy() {
      for (ManagedTask task : executionStates.keySet()) {
         task.destroy();
      }
      
      executorService.shutdown();
   }

   @Override
   public synchronized void submit(ManagedTask task) {
      if (submitted(task)) {
         throw new IllegalStateException("Task already in execution queue.");
      }
      
      final Future execFuture = executorService.submit(task);
      executionStates.put(task, execFuture);
   }

   @Override
   public synchronized boolean submitted(ManagedTask task) {
      final Future taskFuture = executionStates.get(task);
      boolean submitted = false;

      if (taskFuture != null) {
         if (taskFuture.isDone()) {
            executionStates.remove(task);
         } else {
            submitted = true;
         }
      }

      return submitted;
   }
}
