package org.atomnuke.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.plugin.operation.ComplexOperation;
import org.atomnuke.plugin.operation.OperationFailureException;
import org.atomnuke.plugin.proxy.InstanceEnvProxyFactory;
import org.atomnuke.service.gc.ReclaimationHandler;
import org.atomnuke.util.lifecycle.operation.ReclaimOperation;

/**
 *
 * @author zinic
 */
public abstract class AbstractServiceManager implements ServiceManager {

   private static final ComplexOperation<Service, ProvidesOperationArgument> PROVIDES_OPERATION = new ComplexOperation<Service, ProvidesOperationArgument>() {
      @Override
      public void perform(Service instance, ProvidesOperationArgument argument) throws OperationFailureException {
         if (instance.provides(argument.getServiceInterface())) {
            argument.setProvides(true);
         }
      }
   };

   private final Map<String, ManagedService> registeredServicesByName;
   private final Queue<InstanceContext<Service>> pendingServices;
   private final Stack<ManagedService> registeredServices;
   private final InstanceEnvProxyFactory proxyFactory;

   public AbstractServiceManager(InstanceEnvProxyFactory proxyFactory) {
      this.proxyFactory = proxyFactory;

      registeredServicesByName = new HashMap<String, ManagedService>();
      pendingServices = new LinkedList<InstanceContext<Service>>();
      registeredServices = new Stack<ManagedService>();
   }

   protected synchronized int servicesPending() {
      return pendingServices.size();
   }

   protected synchronized InstanceContext<Service> nextPendingService() {
      return !pendingServices.isEmpty() ? pendingServices.poll() : null;
   }

   protected synchronized void queuePendingService(InstanceContext<Service> service) {
      pendingServices.add(service);
   }

   protected Stack<ManagedService> registeredServices() {
      return registeredServices;
   }

   protected abstract void resolve();

   protected void register(String serviceName, ManagedService service) {
      registeredServicesByName.put(serviceName, service);
      registeredServices.push(service);
   }

   @Override
   public synchronized boolean serviceRegistered(Class serviceInterface) {
      final ProvidesOperationArgument argument = new ProvidesOperationArgument(serviceInterface);

      for (ManagedService managedService : registeredServices) {
         managedService.serviceContext().perform(PROVIDES_OPERATION, argument);

         if (argument.provides()) {
            return true;
         }
      }

      return false;
   }

   @Override
   public synchronized boolean nameRegistered(String serviceName) {
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
      final ProvidesOperationArgument argument = new ProvidesOperationArgument(ReclaimationHandler.class);

      for (Iterator<ManagedService> managedServiceItr = registeredServices.iterator(); managedServiceItr.hasNext();) {
         final ManagedService potentialReclaimer = managedServiceItr.next();

         argument.reset();
         potentialReclaimer.serviceContext().perform(PROVIDES_OPERATION, argument);

         if (argument.provides()) {
            managedServiceItr.remove();
            potentialReclaimer.serviceContext().perform(ReclaimOperation.<Service>instance());
         }
      }

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
      final ProvidesOperationArgument argument = new ProvidesOperationArgument(serviceInterface);

      for (ManagedService managedService : registeredServices) {
         argument.reset();

         final InstanceContext<Service> serviceContext = managedService.serviceContext();
         serviceContext.perform(PROVIDES_OPERATION, argument);

         if (argument.provides()) {
            servicesMatchingInterface.add(serviceContext.instance().name());
         }
      }

      return servicesMatchingInterface;
   }
}
