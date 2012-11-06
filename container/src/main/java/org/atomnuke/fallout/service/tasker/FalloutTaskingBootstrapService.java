package org.atomnuke.fallout.service.tasker;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.atomnuke.container.service.annotation.NukeBootstrap;
import org.atomnuke.service.resolution.ResolutionActionType;
import org.atomnuke.service.Service;
import org.atomnuke.service.ServiceManager;
import org.atomnuke.service.ServiceUnavailableException;
import org.atomnuke.service.context.ServiceContext;
import org.atomnuke.service.gc.ReclamationHandler;
import org.atomnuke.service.resolution.ResolutionAction;
import org.atomnuke.service.resolution.ResolutionActionImpl;
import org.atomnuke.util.lifecycle.runnable.ReclaimableRunnable;
import org.atomnuke.task.TaskHandle;
import org.atomnuke.task.manager.TaskTracker;
import org.atomnuke.task.manager.Tasker;
import org.atomnuke.task.manager.impl.ReclaimableRunnableTasker;
import org.atomnuke.task.manager.impl.ThreadSafeTaskTracker;
import org.atomnuke.task.manager.service.TaskingService;
import org.atomnuke.util.TimeValue;
import org.atomnuke.util.lifecycle.InitializationException;
import org.atomnuke.util.remote.AtomicCancellationRemote;
import org.atomnuke.util.remote.CancellationRemote;
import org.atomnuke.util.service.ServiceHandler;

/**
 *
 * @author zinic
 */
@NukeBootstrap
public class FalloutTaskingBootstrapService implements Service {

   private static final String SERVICE_NAME = "org.atomnuke.task.manager.impl.TaskerService";

   private final CancellationRemote taskTrackerCancelRemote;
   private final List<TaskHandle> explicitlyManagedTasks;

   private TaskingService taskingService;

   public FalloutTaskingBootstrapService() {
      taskTrackerCancelRemote = new AtomicCancellationRemote();
      explicitlyManagedTasks = new LinkedList<TaskHandle>();
   }

   @Override
   public String name() {
      return SERVICE_NAME;
   }

   @Override
   public boolean provides(Class serviceInterface) {
      return serviceInterface.isAssignableFrom(TaskingService.class);
   }

   @Override
   public Object instance() {
      return taskingService;
   }

   @Override
   public ResolutionAction resolve(ServiceManager serviceManager) {
      return new ResolutionActionImpl(
              serviceManager.serviceRegistered(ReclamationHandler.class) ? ResolutionActionType.INIT : ResolutionActionType.DEFER);
   }

   @Override
   public void init(ServiceContext contextObject) throws InitializationException {
      try {
         final ReclamationHandler reclamationHandler = ServiceHandler.instance().firstAvailable(contextObject.manager(), ReclamationHandler.class);

         final TaskTracker taskTracker = new ThreadSafeTaskTracker(taskTrackerCancelRemote);
         final Tasker tasker = new ReclaimableRunnableTasker(taskTracker, reclamationHandler);

         // As the tasking service, it's our job to spin up essential polling services?

         // TODO: This feature should be extensible
         explicitlyManagedTasks.add(tasker.pollTask(new ReclaimableRunnable() {
            @Override
            public void run() {
               reclamationHandler.garbageCollect();
            }

            @Override
            public void destroy() {
            }
         }, new TimeValue(15, TimeUnit.MILLISECONDS)));

         taskingService = new FalloutTaskingService(taskTracker, tasker);
      } catch (ServiceUnavailableException sue) {
         throw new InitializationException(sue);
      }
   }

   @Override
   public void destroy() {
      for (TaskHandle handle : explicitlyManagedTasks) {
         handle.cancellationRemote().cancel();
      }

      explicitlyManagedTasks.clear();
      taskTrackerCancelRemote.cancel();
   }
}
