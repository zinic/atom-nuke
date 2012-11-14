package org.atomnuke.service.runtime;

import org.atomnuke.service.Service;

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
   public final boolean provides(Class serviceInterface) {
      final boolean directInherit = serviceInterface.isAssignableFrom(advertisedService);
      final boolean nameMatch = serviceInterface.getName().equals(advertisedService.getName());

      return directInherit || nameMatch;
   }
}
