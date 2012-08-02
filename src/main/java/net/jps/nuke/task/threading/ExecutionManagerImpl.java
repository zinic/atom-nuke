package net.jps.nuke.task.threading;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class ExecutionManagerImpl implements ExecutionManager {

   private static final Logger LOG = LoggerFactory.getLogger(ExecutionManagerImpl.class);
   
   private final List<TaskFuture> executionStates;
   private final ExecutorService executorService;
   private final int executionCap;

   public ExecutionManagerImpl(int executionCap, ExecutorService executorService) {
      this.executionCap = executionCap;
      this.executorService = executorService;
      executionStates = new LinkedList<TaskFuture>();
   }

   private synchronized List<TaskFuture> copyExecutionStates() {
      for (Iterator<TaskFuture> itr = executionStates.iterator(); itr.hasNext();) {
         final TaskFuture nextTask = itr.next();

         if (nextTask.done()) {
            itr.remove();
         }
      }

      return new LinkedList<TaskFuture>(executionStates);
   }

   private synchronized void track(UUID id, Future future) {
      executionStates.add(new TaskFuture(future, id));
   }

   @Override
   public synchronized void destroy() {
      // Shut down the execution pool
      executorService.shutdown();

      try {
         // Try to wait for things to settle
         executorService.awaitTermination(5, TimeUnit.SECONDS);
      } catch (InterruptedException ie) {
         LOG.warn("Interrupted while waiting for task delegates to finish. This may introduce bad state.");
         executorService.shutdownNow();
      }
   }

   @Override
   public UUID submit(Runnable task) {
      final UUID id = UUID.randomUUID();
      submit(id, task);
      
      return id;
   }

   @Override
   public synchronized void submit(UUID id, Runnable task) {
      while (copyExecutionStates().size() >= executionCap) {
         try {
            Thread.yield();
            wait(0, 10000);
         } catch (InterruptedException ie) {
            LOG.warn("Interrupted while waiting for task delegates to free-up. Task will not be scheduled.");
            return;
         }
      }

      track(id, executorService.submit(task));
   }

   @Override
   public boolean submitted(UUID id) {
      for (TaskFuture executingTask : copyExecutionStates()) {
         if (executingTask.id().equals(id)) {
            return true;
         }
      }

      return false;
   }
}
