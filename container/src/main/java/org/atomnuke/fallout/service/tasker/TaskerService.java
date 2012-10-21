package org.atomnuke.fallout.service.tasker;

import org.atomnuke.container.service.annotation.NukeBootstrap;
import org.atomnuke.service.ResolutionAction;
import org.atomnuke.service.Service;
import org.atomnuke.service.ServiceManager;
import org.atomnuke.service.context.ServiceContext;
import org.atomnuke.task.manager.Tasker;
import org.atomnuke.task.manager.impl.TaskerImpl;
import org.atomnuke.util.lifecycle.InitializationException;
import org.atomnuke.util.remote.AtomicCancellationRemote;
import org.atomnuke.util.remote.CancellationRemote;

/**
 *
 * @author zinic
 */
@NukeBootstrap
public class TaskerService implements Service {

   private static final String SERVICE_NAME = "org.atomnuke.task.manager.impl.TaskerService";

   private CancellationRemote cancellationRemote;
   private Tasker tasker;

   @Override
   public String name() {
      return SERVICE_NAME;
   }

   @Override
   public boolean provides(Class serviceInterface) {
      return serviceInterface.isAssignableFrom(Tasker.class);
   }

   @Override
   public Object instance() {
      return tasker;
   }

   @Override
   public ResolutionAction resolve(ServiceManager serviceManager) {
      return ResolutionAction.INIT;
   }

   @Override
   public void init(ServiceContext contextObject) throws InitializationException {
      cancellationRemote = new AtomicCancellationRemote();
      tasker = new TaskerImpl(cancellationRemote);
   }

   @Override
   public void destroy() {
      cancellationRemote.cancel();
      tasker = null;
   }
}
