package net.jps.nuke;

import net.jps.nuke.util.TimeValue;
import net.jps.nuke.task.threading.ExecutionManagerImpl;
import net.jps.nuke.task.ManagedTaskImpl;
import net.jps.nuke.task.Task;
import net.jps.nuke.util.remote.CancellationRemote;
import net.jps.nuke.util.remote.AtomicCancellationRemote;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import net.jps.nuke.source.AtomSource;

/**
 *
 * @author zinic
 */
public class NukeKernel implements Nuke {

   private static final ThreadFactory DEFAULT_THREAD_FACTORY = new ThreadFactory() {
      private long tid = 0;

      @Override
      public Thread newThread(Runnable r) {
         return new Thread(r, "nuke-worker-" + (tid++));
      }
   };
   
   private static final int NUM_PROCESSORS = Runtime.getRuntime().availableProcessors();
   private static long kid = 0;
   
   private final CancellationRemote crawlerCancellationRemote;
   private final ExecutorService executorService;
   private final KernelDelegate logic;
   private final Thread controlThread;

   /**
    * Initializes a new Nuke kernel.
    *
    * This kernel will retain a core execution pool size equal to the number of
    * the processors available on the system. The max size for the execution
    * pool will be equal to the number of processors available to the system
    * multiplied by four.
    */
   public NukeKernel() {
      // Gimme all the processors :E
      this(NUM_PROCESSORS, NUM_PROCESSORS * 4);
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
      this(new ThreadPoolExecutor(corePoolSize, maxPoolsize, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), DEFAULT_THREAD_FACTORY, new NukeRejectionHandler()));
   }

   /**
    * Initializes a new Nuke kernel.
    *
    * @param executorService sets the execution service that the kernel will
    * utilize for scheduling.
    */
   public NukeKernel(ExecutorService executorService) {
      this.executorService = executorService;

      crawlerCancellationRemote = new AtomicCancellationRemote();
      logic = new KernelDelegate(crawlerCancellationRemote, new ExecutionManagerImpl(executorService));
      controlThread = new Thread(logic, "nuke-kernel-" + (kid++));
   }

   @Override
   public void start() {
      if (controlThread.getState() != Thread.State.NEW) {
         throw new IllegalStateException("Crawler already started or destroyed.");
      }

      controlThread.start();
   }

   @Override
   public void destroy() {
      crawlerCancellationRemote.cancel();
      controlThread.interrupt();

      try {
         controlThread.join();
      } catch (InterruptedException ie) {
         // TODO:Log
      }
   }

   @Override
   public Task follow(AtomSource source) {
      return follow(source, new TimeValue(1, TimeUnit.MINUTES));
   }

   @Override
   public Task follow(AtomSource source, TimeValue pollingInterval) {
      final ManagedTaskImpl managedTask = new ManagedTaskImpl(pollingInterval, executorService, source);
      logic.addTask(managedTask);

      return managedTask;
   }
}
