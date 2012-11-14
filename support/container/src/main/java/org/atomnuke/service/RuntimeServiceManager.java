package org.atomnuke.service;

import java.util.Collections;
import org.atomnuke.NukeEnvironment;
import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.plugin.operation.OperationFailureException;
import org.atomnuke.plugin.proxy.InstanceEnvProxyFactory;
import org.atomnuke.service.context.ServiceContextImpl;
import org.atomnuke.service.operation.ServiceInitOperation;
import org.atomnuke.service.operation.ServiceResolutionArgument;
import org.atomnuke.service.operation.ServiceResolveOperation;
import org.atomnuke.lifecycle.resolution.ResolutionAction;
import org.atomnuke.util.remote.AtomicCancellationRemote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class RuntimeServiceManager extends AbstractServiceManager {

   private static final Logger LOG = LoggerFactory.getLogger(RuntimeServiceManager.class);
   private final NukeEnvironment environment;

   public RuntimeServiceManager(NukeEnvironment environment, InstanceEnvProxyFactory proxyFactory) {
      super(proxyFactory);

      this.environment = environment;
   }

   @Override
   public void resolve() {
      /**
       * We can sweep the service registry equal to the number of services that
       * are pending. While we may miss a few services, calls to this method
       * should stack and flush everything out correctly.
       */
      final int totalSweepsAllowed = servicesPending();
      boolean continueResolving = true;

      for (int resolutionSweeps = 0; continueResolving; resolutionSweeps++) {
         final ServiceResolutionArgument resolutionArgument = new ServiceResolutionArgument(this);
         final InstanceContext<Service> pendingService = nextPendingService();

         boolean initializedService = false;

         // Attempt to resolve the service
         try {
            pendingService.perform(ServiceResolveOperation.instance(), resolutionArgument);
         } catch (OperationFailureException ofe) {
            LOG.error("Unable to resolve service: " + pendingService + " - Reason: " + ofe.getMessage(), ofe);
            continue;
         }

         // Decide what to do based on what the service told us about its ability to initialize
         final ResolutionAction action = resolutionArgument.resolutionAction();

         switch (action.type()) {
            case INIT:
               initService(pendingService);
               initializedService = true;
               break;

            case DEFER:
               LOG.info("Service: " + pendingService.instance().name() + " deferred startup.");
               queuePendingService(pendingService);
               break;

            case FAIL:
            default:
               LOG.error("Service: " + pendingService.instance().name() + " failed to start.");
         }

         continueResolving = servicesPending() > 0 && (initializedService || resolutionSweeps < totalSweepsAllowed);
      }
   }

   private void initService(InstanceContext<Service> pendingService) {
      LOG.info("Service: " + pendingService.instance().name() + " initializing.");

      // TODO: Fix parameter passing - this is just terrible :p
      final ServiceContext serviceContext = new ServiceContextImpl(environment, Collections.EMPTY_MAP, this);

      try {
         pendingService.perform(ServiceInitOperation.<Service>instance(), serviceContext);

         final ManagedService managedService = new ManagedService(pendingService, new AtomicCancellationRemote());
         register(pendingService.instance().name(), managedService);
      } catch (OperationFailureException ofe) {
         LOG.error("Unable to initialize service: " + pendingService + " - Reason: " + ofe.getMessage(), ofe);
      }
   }
}
