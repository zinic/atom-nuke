package org.atomnuke.service;

import java.util.Collections;
import org.atomnuke.NukeEnvironment;
import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.plugin.operation.OperationFailureException;
import org.atomnuke.plugin.proxy.InstanceEnvProxyFactory;
import org.atomnuke.service.context.ServiceContextImpl;
import org.atomnuke.service.operation.ServiceInitOperation;
import org.atomnuke.service.operation.ServiceResolutionArgument;
import org.atomnuke.lifecycle.resolution.ResolutionAction;
import org.atomnuke.service.introspection.ServicesInterrogatorImpl;
import org.atomnuke.util.remote.AtomicCancellationRemote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class RuntimeServiceManager extends AbstractServiceManager {

   private static final Logger LOG = LoggerFactory.getLogger(RuntimeServiceManager.class);

   private final ResolutionHandlerImpl resolutionHandler;
   private final NukeEnvironment environment;

   public RuntimeServiceManager(NukeEnvironment environment, InstanceEnvProxyFactory proxyFactory) {
      super(proxyFactory);

      this.environment = environment;
      this.resolutionHandler = new ResolutionHandlerImpl(this);
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

         // There's a possibility that we're trying to resove and the number of pending services has changed drastically
         if (pendingService == null) {
            break;
         }

         boolean initializedService = false;

         // Attempt to resolve the service and decide what to do based on that
         final ResolutionAction action = resolutionHandler.resolve(pendingService);

         switch (action.type()) {
            case INIT:
               initService(pendingService);
               initializedService = true;
               break;

            case DEFER:
               LOG.debug("Service: " + pendingService.instance().name() + " deferred startup.");
               queuePendingService(pendingService);
               break;

            case FAIL:
            default:
               LOG.error("Service: " + pendingService.instance().name() + " failed to start. Fallout will cease attempting to initialize it.");
         }

         continueResolving = servicesPending() > 0 && (initializedService || resolutionSweeps < totalSweepsAllowed);
      }
   }

   private void initService(InstanceContext<Service> pendingService) {
      LOG.info("Service: " + pendingService.instance().name() + " initializing.");

      // TODO: Fix parameter passing - this is just terrible :p
      final ServiceContext serviceContext = new ServiceContextImpl(new ServicesInterrogatorImpl(this), Collections.EMPTY_MAP, environment, this);

      try {
         pendingService.perform(ServiceInitOperation.<Service>instance(), serviceContext);

         final ManagedService managedService = new ManagedService(pendingService, new AtomicCancellationRemote());
         register(pendingService.instance().name(), managedService);
      } catch (OperationFailureException ofe) {
         LOG.error("Unable to initialize service: " + pendingService + " - Reason: " + ofe.getMessage(), ofe);
      }
   }
}
