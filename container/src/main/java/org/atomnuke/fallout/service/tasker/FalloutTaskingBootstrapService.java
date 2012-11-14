package org.atomnuke.fallout.service.tasker;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.atomnuke.container.service.annotation.NukeBootstrap;
import org.atomnuke.fallout.service.gc.GarbageCollectionTask;
import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.plugin.LocalInstanceContext;
import org.atomnuke.lifecycle.resolution.ResolutionActionType;
import org.atomnuke.service.ServiceManager;
import org.atomnuke.service.ServiceUnavailableException;
import org.atomnuke.service.ServiceContext;
import org.atomnuke.service.gc.ReclamationHandler;
import org.atomnuke.lifecycle.resolution.ResolutionAction;
import org.atomnuke.lifecycle.resolution.ResolutionActionImpl;
import org.atomnuke.task.TaskHandle;
import org.atomnuke.task.manager.TaskTracker;
import org.atomnuke.task.manager.Tasker;
import org.atomnuke.task.manager.impl.FalloutTasker;
import org.atomnuke.task.manager.impl.ThreadSafeTaskTracker;
import org.atomnuke.task.manager.service.TaskingService;
import org.atomnuke.util.TimeValue;
import org.atomnuke.lifecycle.InitializationException;
import org.atomnuke.service.runtime.AbstractRuntimeService;
import org.atomnuke.util.remote.AtomicCancellationRemote;
import org.atomnuke.util.remote.CancellationRemote;
import org.atomnuke.util.service.ServiceHandler;

/**
 *
 * @author zinic
 */
@NukeBootstrap
public class FalloutTaskingBootstrapService extends AbstractRuntimeService {

   private static final String SERVICE_NAME = "org.atomnuke.task.manager.impl.TaskerService";

   private final CancellationRemote taskTrackerCancelRemote;
   private final List<TaskHandle> explicitlyManagedTasks;

   private TaskingService taskingService;

   public FalloutTaskingBootstrapService() {
      super(TaskingService.class);

      taskTrackerCancelRemote = new AtomicCancellationRemote();
      explicitlyManagedTasks = new LinkedList<TaskHandle>();
   }

   @Override
   public String name() {
      return SERVICE_NAME;
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
         final ReclamationHandler reclamationHandler = ServiceHandler.instance().firstAvailable(contextObject.services(), ReclamationHandler.class);

         final TaskTracker taskTracker = new ThreadSafeTaskTracker(taskTrackerCancelRemote);
         final Tasker tasker = new FalloutTasker(taskTracker, reclamationHandler);

         // As the tasking service, it's our job to spin up essential polling services?
         final InstanceContext gcTaskCtx = new LocalInstanceContext(new GarbageCollectionTask(reclamationHandler));

         // TODO: This feature should be extensible
         explicitlyManagedTasks.add(tasker.pollTask(gcTaskCtx, new TimeValue(15, TimeUnit.MILLISECONDS)));

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
