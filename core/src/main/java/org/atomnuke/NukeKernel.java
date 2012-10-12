package org.atomnuke;

import org.atomnuke.kernel.KernelDelegate;
import org.atomnuke.kernel.shutdown.KernelShutdownHook;
import org.atomnuke.task.threading.ExecutionManagerImpl;
import org.atomnuke.task.manager.TaskManagerImpl;
import org.atomnuke.task.threading.ExecutionManager;
import org.atomnuke.task.threading.ExecutionQueueImpl;

/**
 * The Nuke kernel contains the references to a TaskManager, a cancellation
 * remote and the runnable kernel logic delegate.
 *
 * This is the primary interface to the underlying event polling system.
 *
 * @author zinic
 */
public class NukeKernel extends AbstractNukeImpl {

   /**
    * Initializes a new Nuke kernel.
    *
    * This kernel will retain a core execution pool size equal to the number of
    * the processors available on the system. The max size for the execution
    * pool will be equal to the number of processors available to the system
    * multiplied by two.
    */
   public NukeKernel() {
      this(new ExecutionManagerImpl());
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
      this(new ExecutionManagerImpl(new ExecutionQueueImpl(corePoolSize, maxPoolsize)));
   }

   /**
    * Initialized a new Nuke kernel driven by the passed execution manager.
    *
    * @param manager
    */
   public NukeKernel(ExecutionManager manager) {
      super(new KernelShutdownHook(), new KernelDelegate(new TaskManagerImpl(manager), manager));
   }
}
