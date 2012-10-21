package org.atomnuke.service;

import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.util.remote.CancellationRemote;

/**
 *
 * @author zinic
 */
public class ManagedService {

   private final InstanceContext<Service> serviceContext;
   private final CancellationRemote cancellationRemote;

   public ManagedService(InstanceContext<Service> serviceContext, CancellationRemote cancellationRemote) {
      this.serviceContext = serviceContext;
      this.cancellationRemote = cancellationRemote;
   }

   public InstanceContext<Service> serviceContext() {
      return serviceContext;
   }

   public CancellationRemote cancellationRemote() {
      return cancellationRemote;
   }
}
