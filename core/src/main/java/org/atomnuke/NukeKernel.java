package org.atomnuke;

import org.atomnuke.kernel.GenericKernelDelegate;
import org.atomnuke.kernel.shutdown.KernelShutdownHook;
import org.atomnuke.service.gc.ReclaimationHandler;
import org.atomnuke.service.gc.impl.NukeReclaimationHandler;
import org.atomnuke.task.manager.AtomTasker;
import org.atomnuke.task.manager.TaskManager;
import org.atomnuke.task.manager.TaskTracker;
import org.atomnuke.task.manager.Tasker;
import org.atomnuke.task.manager.impl.AtomTaskerImpl;
import org.atomnuke.task.manager.impl.GenericTaskManger;
import org.atomnuke.task.manager.impl.ReclaimableRunnableTasker;
import org.atomnuke.task.manager.impl.ThreadSafeTaskTracker;
import org.atomnuke.task.threading.ExecutionManager;
import org.atomnuke.task.threading.ExecutionManagerImpl;
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


   public NukeKernel() {
      this(new ExecutionManagerImpl(), new NukeReclaimationHandler(), new ThreadSafeTaskTracker(new AtomicCancellationRemote()));
   }

   public NukeKernel(ExecutionManager executionManager, ReclaimationHandler reclaimationHandler, TaskTracker taskTracker) {
      this(executionManager, reclaimationHandler, new GenericTaskManger(executionManager, taskTracker), new ReclaimableRunnableTasker(taskTracker, reclaimationHandler));
   }

   /**
    * Initialized a new Nuke kernel driven by the passed execution manager.
    *
    * @param executionManager
    */
   public NukeKernel(ExecutionManager executionManager, ReclaimationHandler reclaimationHandler, TaskManager taskManager, Tasker tasker) {
      super(new KernelShutdownHook(), new GenericKernelDelegate(taskManager));

      atomTasker = new AtomTaskerImpl(reclaimationHandler, executionManager, tasker);
   }

   @Override
   public AtomTasker atomTasker() {
      return atomTasker;
   }
}
