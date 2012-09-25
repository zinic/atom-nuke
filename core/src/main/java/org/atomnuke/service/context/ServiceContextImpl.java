package org.atomnuke.service.context;

import java.util.Map;
import org.atomnuke.service.ServiceManager;

/**
 *
 * @author zinic
 */
public class ServiceContextImpl implements ServiceContext {

   private final Map<String, String> parameters;
   private final ServiceManager services;

   public ServiceContextImpl(Map<String, String> parameters, ServiceManager services) {
      this.parameters = parameters;
      this.services = services;
   }

   @Override
   public ServiceManager services() {
      return services;
   }

   @Override
   public Map<String, String> parameters() {
      return parameters;
   }
}
