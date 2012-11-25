package org.atomnuke.fallout.service.atombus;

import org.atomnuke.container.service.annotation.NukeService;
import org.atomnuke.container.service.annotation.Requires;
import org.atomnuke.service.ServiceUnavailableException;
import org.atomnuke.service.ServiceContext;
import org.atomnuke.service.gc.ReclamationHandler;
import org.atomnuke.task.manager.AtomTasker;
import org.atomnuke.task.manager.impl.AtomTaskerImpl;
import org.atomnuke.task.manager.service.TaskingService;
import org.atomnuke.lifecycle.InitializationException;
import org.atomnuke.service.runtime.AbstractRuntimeService;

/**
 *
 * @author zinic
 */
@NukeService
@Requires({ReclamationHandler.class, TaskingService.class})
public class AtomTaskerService extends AbstractRuntimeService {

   private AtomTasker atomTasker;

   public AtomTaskerService() {
      super(AtomTasker.class);
   }

   @Override
   public Object instance() {
      return atomTasker;
   }

   @Override
   public void init(ServiceContext contextObject) throws InitializationException {
      try {
         final ReclamationHandler reclamationHandler = contextObject.services().firstAvailable(ReclamationHandler.class);
         final TaskingService taskingService = contextObject.services().firstAvailable(TaskingService.class);

         atomTasker = new AtomTaskerImpl(reclamationHandler, taskingService.tasker());
      } catch (ServiceUnavailableException sue) {
         throw new InitializationException(sue);
      }
   }
}
