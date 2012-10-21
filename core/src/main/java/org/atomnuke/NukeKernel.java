package org.atomnuke;

import org.atomnuke.kernel.GenericKernelDelegate;
import org.atomnuke.kernel.shutdown.KernelShutdownHook;
import org.atomnuke.service.gc.ReclaimationHandler;
import org.atomnuke.service.gc.impl.NukeReclaimationHandler;
import org.atomnuke.task.manager.AtomTasker;
import org.atomnuke.task.manager.TaskManager;
import org.atomnuke.task.manager.Tasker;
import org.atomnuke.task.manager.impl.AtomTaskerImpl;
import org.atomnuke.task.manager.impl.GenericTaskManger;
import org.atomnuke.task.manager.impl.TaskerImpl;
import org.atomnuke.task.threading.ExecutionManagerImpl;
import org.atomnuke.task.threading.ExecutionManager;
import org.atomnuke.task.threading.ExecutionQueueImpl;
import org.atomnuke.util.remote.AtomicCancellationRemote;

/**
 * The Nuke kernel contains the references to a TaskManager, a cancellation
 * remote and the runnable kernel logic delegate.
 *
 * This is the primary interface to the underlying event polling system.
 *
 * @author zinic
 */
public class NukeKernel extends AbstractNukeImpl {

   private final AtomTasker atomTasker;
   private final Tasker tasker;

   /**
    * Initializes a new Nuke kernel.
    *
    * This kernel will retain a core execution pool size equal to the number of
    * the processors available on the system. The max size for the execution
    * pool will be equal to the number of processors available to the system
    * multiplied by two.
    */
   public NukeKernel() {
      this(new ExecutionManagerImpl(), new NukeReclaimationHandler(), new TaskerImpl(new AtomicCancellationRemote()));
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
      this(new ExecutionManagerImpl(new ExecutionQueueImpl(corePoolSize, maxPoolsize)), new NukeReclaimationHandler(), new TaskerImpl(new AtomicCancellationRemote()));
   }

   /**
    * Initialized a new Nuke kernel driven by the passed execution manager.
    *
    * @param manager
    */
   public NukeKernel(ExecutionManager manager, ReclaimationHandler reclaimationHandler, Tasker tasker) {
      this(manager, reclaimationHandler, new GenericTaskManger(manager, tasker));
   }

   /**
    * Initialized a new Nuke kernel driven by the passed execution manager.
    *
    * @param executionManager
    */
   public NukeKernel(ExecutionManager executionManager, ReclaimationHandler reclaimationHandler, TaskManager taskManager) {
      super(new KernelShutdownHook(), new GenericKernelDelegate(taskManager, executionManager));

      tasker = taskManager.tasker();
      atomTasker = new AtomTaskerImpl(reclaimationHandler, executionManager, tasker);
   }

   @Override
   public AtomTasker atomTasker() {
      return atomTasker;
   }
}
