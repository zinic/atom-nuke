package org.atomnuke.service.introspection;

import java.util.Collection;
import org.atomnuke.service.ServiceManager;
import org.atomnuke.service.ServiceUnavailableException;

/**
 *
 * @author zinic
 */
public class ServicesInterrogatorImpl implements ServicesInterrogator {

   private final ServiceManager manager;

   public ServicesInterrogatorImpl(ServiceManager manager) {
      this.manager = manager;
   }

   @Override
   public <T> T firstAvailable(Class<T> serviceClass) throws ServiceUnavailableException {
      final Collection<String> registeredServiceNames = manager.servicesAdvertising(serviceClass);

      if (registeredServiceNames.isEmpty()) {
         throw new ServiceUnavailableException(serviceClass, "No service available for service interface: " + serviceClass.getName());
      }

      return manager.get(registeredServiceNames.iterator().next(), serviceClass);
   }

   @Override
   public <T> T lookup(String serviceName, Class<T> serviceClass) throws ServiceUnavailableException {
      return manager.get(serviceName, serviceClass);
   }
}
