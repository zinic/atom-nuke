package org.atomnuke.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.plugin.proxy.InstanceEnvProxyFactory;
import org.atomnuke.util.lifecycle.operation.ReclaimOperation;

/**
 *
 * @author zinic
 */
public abstract class AbstractServiceManager implements ServiceManager {

   private final Map<String, ManagedService> registeredServicesByName;
   private final List<InstanceContext<Service>> pendingServices;
   private final Stack<ManagedService> registeredServices;
   private final InstanceEnvProxyFactory proxyFactory;

   public AbstractServiceManager(InstanceEnvProxyFactory proxyFactory) {
      this.proxyFactory = proxyFactory;

      registeredServicesByName = new HashMap<String, ManagedService>();
      pendingServices = new LinkedList<InstanceContext<Service>>();
      registeredServices = new Stack<ManagedService>();
   }

   protected Collection<InstanceContext<Service>> pendingServices() {
      return pendingServices;
   }

   protected Collection<ManagedService> registeredServices() {
      return registeredServices;
   }

   protected abstract void resolve();

   protected void register(String serviceName, ManagedService service) {
      registeredServicesByName.put(serviceName, service);
      registeredServices.push(service);
   }

   @Override
   public synchronized boolean isRegistered(String serviceName) {
      return registeredServicesByName.containsKey(serviceName);
   }

   @Override
   public synchronized <T> T get(String name, Class<T> serviceInterface) {
      final ManagedService managedService = registeredServicesByName.get(name);

      if (managedService == null) {
         return null;
      }

      if (!managedService.serviceContext().instance().provides(serviceInterface)) {
         throw new IllegalArgumentException("Service: " + name + " does not provide interface: " + serviceInterface.getName());
      }

      return proxyFactory.newServiceProxy(serviceInterface, managedService.serviceContext());
   }

   @Override
   public synchronized void destroy() {
      while (!registeredServices.empty()) {
         final ManagedService managedService = registeredServices.pop();
         managedService.serviceContext().perform(ReclaimOperation.<Service>instance());
      }
   }

   @Override
   public synchronized void register(InstanceContext<Service> service) throws ServiceAlreadyRegisteredException {
      final String serviceName = service.instance().name();

      if (registeredServicesByName.containsKey(serviceName)) {
         throw new ServiceAlreadyRegisteredException(serviceName);
      }

      pendingServices.add(service);

      resolve();
   }

   @Override
   public synchronized Collection<String> listRegisteredServicesFor(Class serviceInterface) {
      final Collection<String> servicesMatchingInterface = new LinkedList<String>();

      for (ManagedService managedService : registeredServices) {
         final InstanceContext<Service> serviceContext = managedService.serviceContext();

         // TODO: Fix this interaction to take environment stepping into account
         if (serviceContext.instance().provides(serviceInterface)) {
            servicesMatchingInterface.add(serviceContext.instance().name());
         }
      }

      return servicesMatchingInterface;
   }
}
