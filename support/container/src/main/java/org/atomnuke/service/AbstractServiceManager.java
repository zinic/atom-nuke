package org.atomnuke.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.plugin.operation.ComplexOperation;
import org.atomnuke.plugin.operation.OperationFailureException;
import org.atomnuke.plugin.proxy.InstanceEnvProxyFactory;
import org.atomnuke.service.gc.ReclamationHandler;
import org.atomnuke.util.lifecycle.operation.ReclaimOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

   private static final Logger LOG = LoggerFactory.getLogger(AbstractServiceManager.class);

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

   protected synchronized void register(String serviceName, ManagedService service) {
      registeredServicesByName.put(serviceName, service);
      registeredServices.push(service);
   }

   @Override
   public synchronized boolean serviceRegistered(Class serviceInterface) {
      final ProvidesOperationArgument argument = new ProvidesOperationArgument(serviceInterface);

      for (ManagedService managedService : registeredServices) {
         try {
            argument.reset();
            managedService.serviceContext().perform(PROVIDES_OPERATION, argument);
         } catch (OperationFailureException ofe) {
            LOG.error("Failed to identify if the given service: " + managedService + " provides the interface: " + serviceInterface.getName() + " - Reason: " + ofe.getMessage(), ofe);
         }

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
      final ProvidesOperationArgument argument = new ProvidesOperationArgument(ReclamationHandler.class);

      while (!registeredServices.empty()) {
         final ManagedService managedService = registeredServices.pop();
         argument.reset();

         try {
            managedService.serviceContext().perform(PROVIDES_OPERATION, argument);

            if (argument.provides()) {
               managedService.serviceContext().perform(ReclaimOperation.<Service>instance());
            }
         } catch (OperationFailureException ofe) {
            LOG.error("Failed to reclaim service: " + managedService + " - Reason: " + ofe.getMessage());
         }
      }
   }

   @Override
   public synchronized void submit(InstanceContext<Service> service) throws ServiceAlreadyRegisteredException {
      final String serviceName = service.instance().name();

      if (registeredServicesByName.containsKey(serviceName)) {
         throw new ServiceAlreadyRegisteredException(serviceName);
      }

      pendingServices.add(service);
   }

   @Override
   public synchronized Collection<String> listRegisteredServicesFor(Class serviceInterface) {
      final Collection<String> servicesMatchingInterface = new LinkedList<String>();
      final ProvidesOperationArgument argument = new ProvidesOperationArgument(serviceInterface);

      for (ManagedService managedService : registeredServices) {
         final InstanceContext<Service> serviceContext = managedService.serviceContext();
         argument.reset();

         try {
            serviceContext.perform(PROVIDES_OPERATION, argument);

            if (argument.provides()) {
               servicesMatchingInterface.add(serviceContext.instance().name());
            }
         } catch (OperationFailureException ofe) {
            LOG.error("Failed to identify if the given service: " + managedService + " provides the interface: " + serviceInterface.getName() + " - Reason: " + ofe.getMessage(), ofe);
         }
      }

      return servicesMatchingInterface;
   }
}
