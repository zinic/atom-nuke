package org.atomnuke.fallout.service;

import java.util.Collection;
import org.atomnuke.service.ServiceManager;

/**
 *
 * @author zinic
 */
public class ServiceHandler {

   private final ServiceManager manager;

   public ServiceHandler(ServiceManager manager) {
      this.manager = manager;
   }

   public <T> T firstAvailable(Class<T> serviceClass) throws ServiceUnavailableException {
      final Collection<String> registeredServiceNames = manager.listRegisteredServicesFor(serviceClass);

      if (registeredServiceNames.isEmpty()) {
         throw new ServiceUnavailableException(serviceClass, "No service available for service interface: " + serviceClass.getName());
      }

      return manager.get(registeredServiceNames.iterator().next(), serviceClass);
   }
}
