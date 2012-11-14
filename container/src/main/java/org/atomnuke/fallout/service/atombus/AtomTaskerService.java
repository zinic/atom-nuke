package org.atomnuke.fallout.service.atombus;

import org.atomnuke.container.service.annotation.NukeService;
import org.atomnuke.service.ServiceManager;
import org.atomnuke.service.ServiceUnavailableException;
import org.atomnuke.service.ServiceContext;
import org.atomnuke.service.gc.ReclamationHandler;
import org.atomnuke.service.resolution.RequiresAction;
import org.atomnuke.lifecycle.resolution.ResolutionAction;
import org.atomnuke.task.manager.AtomTasker;
import org.atomnuke.task.manager.impl.AtomTaskerImpl;
import org.atomnuke.task.manager.service.TaskingService;
import org.atomnuke.lifecycle.InitializationException;
import org.atomnuke.service.runtime.AbstractRuntimeService;
import org.atomnuke.util.service.ServiceHandler;

/**
 *
 * @author zinic
 */
@NukeService
public class AtomTaskerService extends AbstractRuntimeService {

   public static final String SERVICE_NAME = "org.atomnuke.fallout.service.atombus.AtomTaskerService";

   private AtomTasker atomTasker;

   public AtomTaskerService() {
      super(AtomTasker.class);
   }

   @Override
   public String name() {
      return SERVICE_NAME;
   }

   @Override
   public Object instance() {
      return atomTasker;
   }

   @Override
   public ResolutionAction resolve(ServiceManager serviceManager) {
      return new RequiresAction(serviceManager, ReclamationHandler.class, TaskingService.class);
   }

   @Override
   public void init(ServiceContext contextObject) throws InitializationException {
      try {
         final ReclamationHandler reclamationHandler = ServiceHandler.instance().firstAvailable(contextObject.services(), ReclamationHandler.class);
         final TaskingService taskingService = ServiceHandler.instance().firstAvailable(contextObject.services(), TaskingService.class);

         atomTasker = new AtomTaskerImpl(reclamationHandler, taskingService.tasker());
      } catch (ServiceUnavailableException sue) {
         throw new InitializationException(sue);
      }
   }

   @Override
  public void destroy() {
   }
}
