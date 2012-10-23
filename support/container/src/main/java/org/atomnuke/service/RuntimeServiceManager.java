package org.atomnuke.service;

import java.util.Collections;
import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.plugin.proxy.InstanceEnvProxyFactory;
import org.atomnuke.service.context.ServiceContext;
import org.atomnuke.service.context.ServiceContextImpl;
import org.atomnuke.service.operation.ServiceInitOperation;
import org.atomnuke.service.operation.ServiceResolutionArgument;
import org.atomnuke.service.operation.ServiceResolveOperation;
import org.atomnuke.util.remote.AtomicCancellationRemote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class RuntimeServiceManager extends AbstractServiceManager {

   private static final Logger LOG = LoggerFactory.getLogger(RuntimeServiceManager.class);

   public RuntimeServiceManager(InstanceEnvProxyFactory proxyFactory) {
      super(proxyFactory);
   }

   @Override
   protected synchronized void resolve() {
      InstanceContext<Service> pendingService;

      for (int sweeps = servicesPending(); sweeps >= 0 && (pendingService = nextPendingService()) != null; sweeps--) {
         final ServiceResolutionArgument resolutionArgument = new ServiceResolutionArgument(this);
         pendingService.perform(ServiceResolveOperation.instance(), resolutionArgument);

         switch (resolutionArgument.resolutionAction()) {
            case INIT:
               LOG.info("Service: " + pendingService.instance().name() + " initializing.");
               initService(pendingService);
               break;

            case DEFER:
               LOG.info("Service: " + pendingService.instance().name() + " deferred startup.");
               queuePendingService(pendingService);
               break;

            case FAIL:
            default:
               LOG.error("Service: " + pendingService.instance().name() + " failed to start.");
         }
      }
   }

   private void initService(final InstanceContext<Service> pendingService) {
      final ServiceContext serviceContext = new ServiceContextImpl(Collections.EMPTY_MAP, this);

      pendingService.perform(ServiceInitOperation.<Service>instance(), serviceContext);

      final ManagedService managedService = new ManagedService(pendingService, new AtomicCancellationRemote());
      register(pendingService.instance().name(), managedService);
   }
}
