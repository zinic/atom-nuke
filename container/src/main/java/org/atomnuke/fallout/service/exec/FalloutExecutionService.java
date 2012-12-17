package org.atomnuke.fallout.service.exec;

import org.atomnuke.container.service.annotation.NukeBootstrap;
import org.atomnuke.lifecycle.InitializationException;
import org.atomnuke.service.ServiceContext;
import org.atomnuke.service.runtime.AbstractRuntimeService;
import org.atomnuke.task.threading.ExecutionManager;
import org.atomnuke.task.threading.ExecutionManagerImpl;
import org.atomnuke.task.threading.ExecutionQueueImpl;

/**
 *
 * @author zinic
 */
@NukeBootstrap
public class FalloutExecutionService extends AbstractRuntimeService {

   private ExecutionManager executionManager;

   public FalloutExecutionService() {
      super(ExecutionManager.class);
   }
   
   @Override
   public void init(ServiceContext context) throws InitializationException {
      executionManager = new ExecutionManagerImpl(new ExecutionQueueImpl(context.environment()));
   }

   @Override
   public void destroy() {
      executionManager.destroy();
   }

   @Override
   public Object instance() {
      return executionManager;
   }
}
