package org.atomnuke.task.threading;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
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
   
   private final List<TaskFuture> taskExecutionFutures;
   private final BlockingQueue<Runnable> runQueue;
   private final ExecutorService executorService;
   private final int runQueueCapacity;

   public ExecutionManagerImpl(int runQueueCapacity, BlockingQueue<Runnable> runQueue, ExecutorService executorService) {
      this.runQueue = runQueue;
      this.runQueueCapacity = runQueueCapacity;
      this.executorService = executorService;

      taskExecutionFutures = new LinkedList<TaskFuture>();
   }

   private synchronized List<TaskFuture> copyExecutionStates() {
      for (Iterator<TaskFuture> itr = taskExecutionFutures.iterator(); itr.hasNext();) {
         final TaskFuture nextTask = itr.next();

         if (nextTask.done()) {
            itr.remove();
         }
      }

      return new LinkedList<TaskFuture>(taskExecutionFutures);
   }

   private synchronized void track(UUID id, Future future) {
      taskExecutionFutures.add(new TaskFuture(future, id));
   }

   @Override
   public boolean draining() {
      return runQueue.size() > runQueueCapacity;
   }

   @Override
   public void destroy() {
      // Kill all of the queued tasks
      LOG.info("Clearing: " + runQueue.size() + " tasks");
      runQueue.clear();
      
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
   public void queue(Runnable task) {
      executorService.submit(task);
   }

   @Override
   public void submit(UUID id, Runnable task) {
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
