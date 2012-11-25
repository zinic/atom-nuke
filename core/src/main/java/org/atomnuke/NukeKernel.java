package org.atomnuke;

import org.atomnuke.kernel.GenericKernelDelegate;
import org.atomnuke.kernel.shutdown.KernelShutdownHook;
import org.atomnuke.service.gc.ReclamationHandler;
import org.atomnuke.service.gc.impl.NukeReclamationHandler;
import org.atomnuke.task.manager.AtomTasker;
import org.atomnuke.task.manager.TaskManager;
import org.atomnuke.task.manager.TaskTracker;
import org.atomnuke.task.manager.Tasker;
import org.atomnuke.task.manager.impl.AtomTaskerImpl;
import org.atomnuke.task.manager.impl.GenericTaskManger;
import org.atomnuke.task.manager.impl.FalloutTasker;
import org.atomnuke.task.manager.impl.ThreadSafeTaskTracker;
import org.atomnuke.task.threading.ExecutionManager;
import org.atomnuke.task.threading.ExecutionManagerImpl;
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


   public NukeKernel() {
      this(StaticNukeEnvironment.get(), new ExecutionManagerImpl(new ExecutionQueueImpl(StaticNukeEnvironment.get())), new NukeReclamationHandler(), new ThreadSafeTaskTracker(new AtomicCancellationRemote()));
   }

   public NukeKernel(NukeEnvironment nukeEnvironment, ExecutionManager executionManager, ReclamationHandler reclamationHandler, TaskTracker taskTracker) {
      this(nukeEnvironment, executionManager, reclamationHandler, new GenericTaskManger(executionManager, taskTracker), new FalloutTasker(taskTracker, reclamationHandler));
   }

   /**
    * Initialized a new Nuke kernel driven by the passed execution manager.
    *
    * @param executionManager
    */
   public NukeKernel(NukeEnvironment nukeEnvironment, ExecutionManager executionManager, ReclamationHandler reclamationHandler, TaskManager taskManager, Tasker tasker) {
      super(nukeEnvironment, new KernelShutdownHook(), new GenericKernelDelegate(taskManager));

      atomTasker = new AtomTaskerImpl(reclamationHandler, tasker);
   }

   @Override
   public AtomTasker atomTasker() {
      return atomTasker;
   }
}
