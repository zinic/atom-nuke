package org.atomnuke;

import org.atomnuke.kernel.GenericKernelDelegate;
import org.atomnuke.kernel.shutdown.KernelShutdownHook;
import org.atomnuke.task.manager.AtomTasker;
import org.atomnuke.task.manager.TaskManager;
import org.atomnuke.task.manager.impl.AtomTaskerImpl;
import org.atomnuke.task.manager.impl.GenericTaskManger;
import org.atomnuke.task.threading.ExecutionManagerImpl;
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

   private final AtomTasker atomTasker;

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
      this(manager, new GenericTaskManger(manager));
   }

   /**
    * Initialized a new Nuke kernel driven by the passed execution manager.
    *
    * @param executionManager
    */
   public NukeKernel(ExecutionManager executionManager, TaskManager taskManager) {
      super(new KernelShutdownHook(), new GenericKernelDelegate(taskManager, executionManager));

      atomTasker = new AtomTaskerImpl(executionManager, taskManager.tracker());
   }

   @Override
   public AtomTasker tasker() {
      return atomTasker;
   }
}
