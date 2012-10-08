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

   private final List<InstanceContext<Service>> registeredServices;
   private final InstanceEnvProxyFactory proxyFactory;

   public ServiceManagerImpl() {
      this(JapiProxyFactory.getInstance());
   }

   public ServiceManagerImpl(InstanceEnvProxyFactory proxyFactory) {
      this.proxyFactory = proxyFactory;
      registeredServices = new LinkedList<InstanceContext<Service>>();
   }

   @Override
   public synchronized void destroy() {
      for (InstanceContext<Service> serviceCtx : registeredServices) {
         try {
            serviceCtx.environment().stepInto();

            serviceCtx.instance().destroy();
         } catch (Exception ex) {
            LOG.error("Failure in destroying container service, \"" + serviceCtx.instance().name() + "\" - Reason: " + ex.getMessage(), ex);
         } finally {
            serviceCtx.environment().stepOut();
         }
      }
   }

   @Override
   public synchronized void register(InstanceContext<Service> service) {
      registeredServices.add(service);
   }

   @Override
   public synchronized <T> T findService(Class<T> serviceInterface) {
      for (InstanceContext<Service> service : registeredServices) {
         if (service.instance().provides(serviceInterface)) {
            return (T) proxyFactory.newServiceProxy(serviceInterface, service);
         }
      }

      return null;
   }
}
