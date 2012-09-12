package org.atomnuke;

import org.atomnuke.kernel.KernelDelegate;
import org.atomnuke.kernel.KernelShutdownHook;
import org.atomnuke.kernel.NukeRejectionHandler;
import org.atomnuke.kernel.NukeThreadPoolExecutor;
import java.util.concurrent.BlockingQueue;
import org.atomnuke.task.threading.ExecutionManagerImpl;
import org.atomnuke.util.remote.CancellationRemote;
import org.atomnuke.util.remote.AtomicCancellationRemote;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.atomnuke.context.InstanceContext;
import org.atomnuke.context.SimpleInstanceContext;
import org.atomnuke.source.AtomSource;
import org.atomnuke.task.Task;
import org.atomnuke.task.lifecycle.InitializationException;
import org.atomnuke.task.manager.TaskManager;
import org.atomnuke.task.manager.TaskManagerImpl;
import org.atomnuke.task.threading.ExecutionManager;
import org.atomnuke.util.TimeValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Nuke kernel contains the references to a TaskManager, a cancellation
 * remote and the runnable kernel logic delegate.
 *
 * This is the primary interface to the underlying event polling system.
 *
 * @author zinic
 */
public class NukeKernel implements Nuke {

   private static final Logger LOG = LoggerFactory.getLogger(NukeKernel.class);
   private static final ThreadFactory DEFAULT_THREAD_FACTORY = new ThreadFactory() {
      @Override
      public Thread newThread(Runnable r) {
         return new Thread(r, "nuke-worker-" + TID.incrementAndGet());
      }
   };
   private static final int MAX_QUEUE_SIZE = 256000;
   private static final int NUM_PROCESSORS = Runtime.getRuntime().availableProcessors();
   private static final AtomicLong TID = new AtomicLong(0);
   private final CancellationRemote kernelCancellationRemote;
   private final TaskManager taskManager;
   private final KernelDelegate logic;
   private final Thread controlThread;

   /**
    * Initializes a new Nuke kernel.
    *
    * This kernel will retain a core execution pool size equal to the number of
    * the processors available on the system. The max size for the execution
    * pool will be equal to the number of processors available to the system
    * multiplied by two.
    */
   public NukeKernel() {
      // Gimme all the processors :E
      this(NUM_PROCESSORS, NUM_PROCESSORS * 2);
   }

   /**
    * Initializes a new Nuke kernel.
    *
    * @param corePoolSize sets the number of threads that the execution pool
    * will retain during normal operation.
    * @param maxPoolsize sets the maximum number of threads that the execution
    * pool may spawn.
    */
   public NukeKernel(int corePoolSize, int maxPoolsize) {
      final BlockingQueue<Runnable> runQueue = new LinkedBlockingQueue<Runnable>();
      final ExecutorService execService = new NukeThreadPoolExecutor(corePoolSize, maxPoolsize, 30, TimeUnit.SECONDS, runQueue, DEFAULT_THREAD_FACTORY, new NukeRejectionHandler());
      final ExecutionManager executionManager = new ExecutionManagerImpl(MAX_QUEUE_SIZE, runQueue, execService);

      kernelCancellationRemote = new AtomicCancellationRemote();
      taskManager = new TaskManagerImpl(executionManager);
      logic = new KernelDelegate(kernelCancellationRemote, executionManager, taskManager);
      controlThread = new Thread(logic, "nuke-kernel-" + TID.incrementAndGet());

      Runtime.getRuntime().addShutdownHook(new Thread(new KernelShutdownHook(this)));
   }

   @Override
   public Task follow(AtomSource source, TimeValue pollingInterval) throws InitializationException {
      return follow(new SimpleInstanceContext<AtomSource>(source), pollingInterval);
   }

   @Override
   public Task follow(InstanceContext<? extends AtomSource> source, TimeValue pollingInterval) throws InitializationException {
      return taskManager.follow(source, pollingInterval);
   }

   @Override
   public void start() {
      if (controlThread.getState() != Thread.State.NEW) {
         throw new IllegalStateException("Crawler already started or destroyed.");
      }

      LOG.info("Nuke kernel: " + toString() + " starting.");

      taskManager.init();
      controlThread.start();
   }

   @Override
   public void destroy() {
      taskManager.destroy();

      kernelCancellationRemote.cancel();

      try {
         controlThread.join();
      } catch (InterruptedException ie) {
         LOG.info("Nuke kernel interrupted while shutting down. Killing thread now.", ie);
         controlThread.interrupt();
      }
   }
}
