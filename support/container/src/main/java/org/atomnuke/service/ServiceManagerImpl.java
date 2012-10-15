package org.atomnuke.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
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

   private final Map<String, InstanceContext<Service>> registeredServices;
   private final InstanceEnvProxyFactory proxyFactory;

   public ServiceManagerImpl() {
      this(JapiProxyFactory.getInstance());
   }

   public ServiceManagerImpl(InstanceEnvProxyFactory proxyFactory) {
      this.proxyFactory = proxyFactory;

      registeredServices = new HashMap<String, InstanceContext<Service>>();
   }

   @Override
   public synchronized Collection<String> listRegisteredServicesFor(Class serviceInterface) {
      final Collection<String> servicesMatchingInterface = new LinkedList<String>();

      for (InstanceContext<Service> service : registeredServices.values()) {
         if (service.instance().provides(serviceInterface)) {
            servicesMatchingInterface.add(service.instance().name());
         }
      }

      return servicesMatchingInterface;
   }

   @Override
   public synchronized void destroy() {
      for (InstanceContext<Service> serviceCtx : registeredServices.values()) {
         try {
            serviceCtx.environment().stepInto();

            serviceCtx.instance().destroy();
         } catch (Exception ex) {
            LOG.error("Failure in destroying container service, \"" + serviceCtx.instance().name() + "\" - Reason: " + ex.getMessage(), ex);
         } finally {
            serviceCtx.environment().stepOut();
         }
      }

      registeredServices.clear();
   }

   @Override
   public synchronized void register(InstanceContext<Service> service) throws ServiceAlreadyRegisteredException {
      final String serviceName = service.instance().name();

      if (registeredServices.containsKey(serviceName)) {
         throw new ServiceAlreadyRegisteredException(serviceName);
      }

      registeredServices.put(serviceName, service);
   }

   @Override
   public synchronized boolean isRegistered(String serviceName) {
      return registeredServices.containsKey(serviceName);
   }

   @Override
   public synchronized <T> T get(String name, Class<T> serviceInterface) {
      final InstanceContext<Service> serviceInstanceContext = registeredServices.get(name);

      if (!serviceInstanceContext.instance().provides(serviceInterface)) {
         throw new IllegalArgumentException("Service: " + name + " does not provide interface: " + serviceInterface.getName());
      }

      return serviceInstanceContext != null ? (T) proxyFactory.newServiceProxy(serviceInterface, serviceInstanceContext) : null;
   }
}
