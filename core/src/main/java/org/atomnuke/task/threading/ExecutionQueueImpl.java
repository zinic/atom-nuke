package org.atomnuke.task.threading;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.atomnuke.NukeEnv;
import org.atomnuke.kernel.NukeRejectionHandler;
import org.atomnuke.kernel.NukeThreadPoolExecutor;
import org.atomnuke.util.TimeValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class ExecutionQueueImpl implements ExecutionQueue {

   private static final AtomicLong TID = new AtomicLong(0);
   private static final ThreadFactory DEFAULT_THREAD_FACTORY = new ThreadFactory() {
      @Override
      public Thread newThread(Runnable r) {
         return new Thread(r, "nuke-worker-" + TID.incrementAndGet());
      }
   };

   private static final TimeValue DEFAULT_THREAD_KEEPALIVE = new TimeValue(30, TimeUnit.SECONDS);
   private static final Logger LOG = LoggerFactory.getLogger(ExecutionQueueImpl.class);

   private static final int MAX_THREADS = NukeEnv.NUM_PROCESSORS * 2;
   private static final int DEFAULT_QUEUE_SIZE = 256000;

   private final BlockingQueue<Runnable> executionQueue;
   private final ExecutorService executorService;

   public ExecutionQueueImpl() {
      this(NukeEnv.NUM_PROCESSORS, MAX_THREADS);
   }

   /**
    *
    * @param corePoolSize sets the number of threads that the execution pool
    * will retain during normal operation.
    * @param maxPoolsize sets the maximum number of threads that the execution
    * pool may spawn.
    */
   public ExecutionQueueImpl(int corePoolSize, int maxPoolSize) {
      executionQueue = new LinkedBlockingQueue<Runnable>();
      executorService = new NukeThreadPoolExecutor(corePoolSize, maxPoolSize, DEFAULT_THREAD_KEEPALIVE, executionQueue, DEFAULT_THREAD_FACTORY, new NukeRejectionHandler());
   }

   @Override
   public void destroy() {
      LOG.info("Clearing: " + executionQueue.size() + " scheduled tasks");

      // Clear any scheduled tasks
      executionQueue.clear();

      // Shutdown our executor service
      executorService.shutdown();

      try {
         // Try to wait for things to settle
         executorService.awaitTermination(5, TimeUnit.SECONDS);
      } catch (InterruptedException ie) {
         LOG.error("Interrupted while waiting for task delegates to finish. This may introduce bad state.", ie);
         executorService.shutdownNow();
      }
   }

   @Override
   public boolean isFull() {
      return executionQueue.size() > DEFAULT_QUEUE_SIZE;
   }

   @Override
   public Future submit(Runnable r) {
      return executorService.submit(r);
   }

   @Override
   public <T> Future<T> submit(Runnable r, T stateObject) {
      return submit(r, stateObject);
   }
}
