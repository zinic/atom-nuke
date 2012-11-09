package org.atomnuke.service.context;

import org.atomnuke.service.ServiceContext;
import java.util.Map;
import org.atomnuke.service.ServiceManager;

/**
 *
 * @author zinic
 */
public class ServiceContextImpl implements ServiceContext {

   private final Map<String, String> parameters;
   private final ServiceManager manager;

   public ServiceContextImpl(Map<String, String> parameters, ServiceManager manager) {
      this.parameters = parameters;
      this.manager = manager;
   }

   @Override
   public ServiceManager manager() {
      return manager;
   }

   @Override
   public Map<String, String> parameters() {
      return parameters;
   }
}
