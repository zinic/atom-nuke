package org.atomnuke.service;

import java.util.LinkedList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class ServiceManagerImpl implements ServiceManager {

   private static final Logger LOG = LoggerFactory.getLogger(ServiceManagerImpl.class);

   private final List<Service> registeredServices;

   public ServiceManagerImpl() {
      registeredServices = new LinkedList<Service>();
   }

   @Override
   public synchronized void destroy() {
      for (Service service : registeredServices) {
         try {
            service.destroy();
         } catch (Exception ex) {
            LOG.error("Failure in destroying container service, \"" + service.name() + "\" - Reason: " + ex.getMessage(), ex);
         }
      }
   }

   @Override
   public synchronized void register(Service serviceInterface) {
      registeredServices.add(serviceInterface);
   }

   @Override
   public synchronized <T> T findService(Class<T> serviceInterface) {
      for (Service service : registeredServices) {
         if (serviceInterface.isAssignableFrom(service.instance().getClass())) {
            return serviceInterface.cast(service.instance());
         }
      }

      return null;
   }
}
