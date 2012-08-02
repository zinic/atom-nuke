package net.jps.nuke;

import java.util.concurrent.BlockingQueue;
import net.jps.nuke.task.threading.ExecutionManagerImpl;
import net.jps.nuke.util.remote.CancellationRemote;
import net.jps.nuke.util.remote.AtomicCancellationRemote;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import net.jps.nuke.task.manager.TaskManager;
import net.jps.nuke.task.manager.TaskManagerImpl;
import net.jps.nuke.task.Tasker;
import net.jps.nuke.task.threading.ExecutionManager;

/**
 *
 * @author zinic
 */
public class NukeKernel implements Nuke {

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
      final BlockingQueue<Runnable> runQueue = new LinkedBlockingQueue<Runnable>();
      final ExecutorService execService = new NukeThreadPoolExecutor(corePoolSize, maxPoolsize, 30, TimeUnit.SECONDS, runQueue, DEFAULT_THREAD_FACTORY, new NukeRejectionHandler());
      final ExecutionManager executionManager = new ExecutionManagerImpl(MAX_QUEUE_SIZE, runQueue, execService);
      
      kernelCancellationRemote = new AtomicCancellationRemote();
      taskManager = new TaskManagerImpl(executionManager);
      logic = new KernelDelegate(kernelCancellationRemote, executionManager, taskManager);
      controlThread = new Thread(logic, "nuke-kernel-" + TID.incrementAndGet());
   }

   @Override
   public Tasker tasker() {
      return taskManager;
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
      kernelCancellationRemote.cancel();

      try {
         controlThread.join();
      } catch (InterruptedException ie) {
         controlThread.interrupt();
      }
      
      taskManager.destroy();
   }
}
