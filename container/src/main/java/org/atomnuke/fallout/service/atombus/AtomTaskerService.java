package org.atomnuke.fallout.service.atombus;

import org.atomnuke.container.service.annotation.NukeService;
import org.atomnuke.service.Service;
import org.atomnuke.service.ServiceManager;
import org.atomnuke.service.ServiceUnavailableException;
import org.atomnuke.service.context.ServiceContext;
import org.atomnuke.service.gc.ReclamationHandler;
import org.atomnuke.service.resolution.ResolutionAction;
import org.atomnuke.service.resolution.ResolutionActionImpl;
import org.atomnuke.service.resolution.ResolutionActionType;
import org.atomnuke.task.manager.AtomTasker;
import org.atomnuke.task.manager.impl.AtomTaskerImpl;
import org.atomnuke.task.manager.service.TaskingService;
import org.atomnuke.util.lifecycle.InitializationException;
import org.atomnuke.util.service.ServiceHandler;

/**
 *
 * @author zinic
 */
@NukeService
public class AtomTaskerService implements Service {

   public static final String SERVICE_NAME = "org.atomnuke.fallout.service.atombus.AtomTaskerService";

   private AtomTasker atomTasker;

   @Override
   public String name() {
      return SERVICE_NAME;
   }

   @Override
   public boolean provides(Class serviceInterface) {
      return serviceInterface.isAssignableFrom(AtomTasker.class);
   }

   @Override
   public Object instance() {
      return atomTasker;
   }

   @Override
   public ResolutionAction resolve(ServiceManager serviceManager) {
      final boolean hasReclamationService = serviceManager.serviceRegistered(ReclamationHandler.class);
      final boolean hasTaskingService = serviceManager.serviceRegistered(TaskingService.class);

      return hasTaskingService && hasReclamationService ? new ResolutionActionImpl(ResolutionActionType.INIT) : new ResolutionActionImpl(ResolutionActionType.FAIL);
   }

   @Override
   public void init(ServiceContext contextObject) throws InitializationException {
      try {
         final ReclamationHandler reclamationHandler = ServiceHandler.instance().firstAvailable(contextObject.manager(), ReclamationHandler.class);
         final TaskingService taskingService = ServiceHandler.instance().firstAvailable(contextObject.manager(), TaskingService.class);

         atomTasker = new AtomTaskerImpl(reclamationHandler, taskingService.tasker());
      } catch (ServiceUnavailableException sue) {
         throw new InitializationException(sue);
      }
   }

   @Override
  public void destroy() {
   }
}
