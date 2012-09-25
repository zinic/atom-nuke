package org.atomnuke;

import java.util.concurrent.atomic.AtomicLong;
import org.atomnuke.plugin.InstanceEnvironment;
import org.atomnuke.plugin.local.LocalInstanceEnvironment;
import org.atomnuke.kernel.KernelDelegate;
import org.atomnuke.kernel.resource.Destroyable;
import org.atomnuke.kernel.shutdown.ShutdownHook;
import org.atomnuke.kernel.shutdown.KernelShutdownHook;
import org.atomnuke.source.AtomSource;
import org.atomnuke.task.Task;
import org.atomnuke.task.manager.TaskManager;
import org.atomnuke.task.threading.ExecutionManager;
import org.atomnuke.util.TimeValue;
import org.atomnuke.util.remote.AtomicCancellationRemote;
import org.atomnuke.util.remote.CancellationRemote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class EmbeddedNukeKernel implements Nuke {

   private static final Logger LOG = LoggerFactory.getLogger(NukeKernel.class);

   private static final long MAX_WAIT_TIME_FOR_SHUTDOWN = 15000;
   private static final AtomicLong TID = new AtomicLong(0);

   private final CancellationRemote kernelCancellationRemote;
   private final ShutdownHook kernelShutdownHook;
   private final TaskManager taskManager;
   private final KernelDelegate logic;
   private final Thread controlThread;

   /**
    * Initializes a new Nuke kernel.
    *
    * @param corePoolSize sets the number of threads that the execution pool
    * will retain during normal operation.
    * @param maxPoolsize sets the maximum number of threads that the execution
    * pool may spawn.
    */
   public EmbeddedNukeKernel(ExecutionManager executionManager, TaskManager taskManager) {
      this.taskManager = taskManager;

      kernelCancellationRemote = new AtomicCancellationRemote();
      logic = new KernelDelegate(kernelCancellationRemote, executionManager, taskManager);
      controlThread = new Thread(logic, "nuke-kernel-" + TID.incrementAndGet());

      kernelShutdownHook = new KernelShutdownHook();
   }

   public ShutdownHook shutdownHook() {
      return kernelShutdownHook;
   }

   @Override
   public Task follow(AtomSource source, TimeValue pollingInterval) {
      return follow(new LocalInstanceEnvironment<AtomSource>(source), pollingInterval);
   }

   @Override
   public Task follow(InstanceEnvironment<AtomSource> source, TimeValue pollingInterval) {
      return taskManager.follow(source, pollingInterval);
   }

   @Override
   public void start() {
      if (controlThread.getState() != Thread.State.NEW) {
         throw new IllegalStateException("Crawler already started or destroyed.");
      }

      kernelShutdownHook.enlist(new Destroyable() {
         @Override
         public void destroy() {
            taskManager.destroy();
            kernelCancellationRemote.cancel();

            try {
               controlThread.join(MAX_WAIT_TIME_FOR_SHUTDOWN);
            } catch (InterruptedException ie) {
               LOG.info("Nuke kernel interrupted while shutting down. Killing thread now.", ie);

               controlThread.interrupt();
            }
         }
      });

      LOG.info("Nuke kernel: " + toString() + " starting.");

      taskManager.init();
      controlThread.start();
   }

   @Override
   public void destroy() {
      kernelShutdownHook.shutdown();
   }
}