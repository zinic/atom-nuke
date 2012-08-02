package net.jps.nuke.threading;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import net.jps.nuke.listener.driver.RegisteredListenerDriver;
import net.jps.nuke.task.ManagedTask;
import net.jps.nuke.task.context.TaskContext;
import net.jps.nuke.task.lifecycle.DestructionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class ExecutionManagerImpl implements ExecutionManager {

   private static final Logger LOG = LoggerFactory.getLogger(ExecutionManagerImpl.class);
   private final List<ExecutingTask> executionStates;
   private final ExecutorService executorService;
   private final int executionCap;

   public ExecutionManagerImpl(int executionCap, ExecutorService executorService) {
      this.executionCap = executionCap;
      this.executorService = executorService;
      executionStates = new LinkedList<ExecutingTask>();
   }

   private synchronized List<ExecutingTask> copyExecutionStates() {
      for (Iterator<ExecutingTask> itr = executionStates.iterator(); itr.hasNext();) {
         final ExecutingTask nextTask = itr.next();

         if (nextTask.done()) {
            itr.remove();
         }
      }

      return new LinkedList<ExecutingTask>(executionStates);
   }

   private synchronized void track(ManagedTask task, Future future) {
      executionStates.add(new ExecutingTask(future, task));
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
   public void submit(RegisteredListenerDriver listenerDriver) {
      executorService.submit(listenerDriver);
   }

   @Override
   public synchronized void submit(ManagedTask task) {
      while (copyExecutionStates().size() >= executionCap) {
         try {
            Thread.yield();
            wait(0, 10000);
         } catch (InterruptedException ie) {
            LOG.warn("Interrupted while waiting for task delegates to free-up. Task will not be scheduled.");
            return;
         }
      }

      track(task, executorService.submit(task));
   }

   @Override
   public boolean submitted(ManagedTask task) {
      for (ExecutingTask executingTask : copyExecutionStates()) {
         if (executingTask.taskLifeCycle().equals(task)) {
            return true;
         }
      }

      return false;
   }
}
