package org.atomnuke.util.service;

import java.util.Collection;
import org.atomnuke.service.ServiceManager;
import org.atomnuke.service.ServiceUnavailableException;

/**
 *
 * @author zinic
 */
public final class ServiceHandler {

   private static final ServiceHandler INSTANCE = new ServiceHandler();

   public static ServiceHandler instance() {
      return INSTANCE;
   }

   private ServiceHandler() {
   }

   public <T> T firstAvailable(ServiceManager manager, Class<T> serviceClass) throws ServiceUnavailableException {
      final Collection<String> registeredServiceNames = manager.listRegisteredServicesFor(serviceClass);

      if (registeredServiceNames.isEmpty()) {
         throw new ServiceUnavailableException(serviceClass, "No service available for service interface: " + serviceClass.getName());
      }

      return manager.get(registeredServiceNames.iterator().next(), serviceClass);
   }
}
