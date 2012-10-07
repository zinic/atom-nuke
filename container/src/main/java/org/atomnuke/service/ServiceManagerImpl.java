package org.atomnuke.service;

import java.util.LinkedList;
import java.util.List;
import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.plugin.proxy.InstanceEnvProxyFactory;
import org.atomnuke.plugin.proxy.japi.JapiProxyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class ServiceManagerImpl implements ServiceManager {

   private static final Logger LOG = LoggerFactory.getLogger(ServiceManagerImpl.class);

   private final InstanceEnvProxyFactory proxyFactory;
   private final List<Service> registeredServices;

   public ServiceManagerImpl() {
      this(JapiProxyFactory.getInstance());
   }

   public ServiceManagerImpl(InstanceEnvProxyFactory proxyFactory) {
      this.proxyFactory = proxyFactory;
      registeredServices = new LinkedList<Service>();
   }

   @Override
   public synchronized void destroy() {
      for (Service service : registeredServices) {
         try {
            service.instanceContext().environment().stepInto();
            service.instanceContext().instance().destroy();
         } catch (Exception ex) {
            LOG.error("Failure in destroying container service, \"" + service.name() + "\" - Reason: " + ex.getMessage(), ex);
         } finally {
            service.instanceContext().environment().stepOut();
         }
      }
   }

   @Override
   public synchronized void register(Service service) {
      registeredServices.add(service);
   }

   @Override
   public synchronized <T> T findService(Class<T> serviceInterface) {
      for (Service service : registeredServices) {
         final ServiceLifeCycle serviceLifeCycle = service.instanceContext().instance();

         if (serviceLifeCycle.provides(serviceInterface)) {
            return (T) proxyFactory.newServiceProxy(serviceInterface, service);
         }
      }

      return null;
   }
}
