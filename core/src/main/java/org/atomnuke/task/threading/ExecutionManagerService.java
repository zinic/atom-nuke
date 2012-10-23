package org.atomnuke.task.threading;

import org.atomnuke.service.ResolutionAction;
import org.atomnuke.service.Service;
import org.atomnuke.service.ServiceManager;
import org.atomnuke.service.context.ServiceContext;
import org.atomnuke.util.lifecycle.InitializationException;

/**
 *
 * @author zinic
 */
public class ExecutionManagerService implements Service {

   private final String SERIVCE_NAME = "org.atomnuke.task.threading.ExecutionService";

   @Override
   public String name() {
      return SERIVCE_NAME;
   }

   @Override
   public boolean provides(Class serviceInterface) {
      return serviceInterface.isAssignableFrom(ExecutionManager.class);
   }

   @Override
   public Object instance() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public ResolutionAction resolve(ServiceManager serviceManager) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public void init(ServiceContext contextObject) throws InitializationException {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public void destroy() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }
}
