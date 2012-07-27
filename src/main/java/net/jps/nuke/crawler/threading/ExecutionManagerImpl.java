package net.jps.nuke.crawler.threading;

import net.jps.nuke.crawler.task.ManagedTask;
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
   public void destroy() {
      executorService.shutdown();
   }

   @Override
   public synchronized void submit(ManagedTask task) {
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
