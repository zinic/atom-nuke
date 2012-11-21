package org.atomnuke.service.runtime;

import org.atomnuke.lifecycle.InitializationException;
import org.atomnuke.service.Service;
import org.atomnuke.service.ServiceContext;

/**
 *
 * @author zinic
 */
public abstract class AbstractRuntimeService implements Service {

   private final Class advertisedService;

   public AbstractRuntimeService(Class advertisedService) {
      this.advertisedService = advertisedService;
   }

   @Override
   public void init(ServiceContext context) throws InitializationException {
   }

   @Override
   public void destroy() {
   }

   @Override
   public String name() {
      return advertisedService.toString();
   }

   @Override
   public final boolean provides(Class serviceInterface) {
      final boolean directInherit = serviceInterface.isAssignableFrom(advertisedService);
      final boolean nameMatch = serviceInterface.getName().equals(advertisedService.getName());

      return directInherit || nameMatch;
   }
}
