package org.atomnuke.service;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author zinic
 */
public class ProvidesOperationArgument {

   private final Class serviceInterface;
   private final AtomicBoolean provides;

   public ProvidesOperationArgument(Class serviceInterface) {
      this.serviceInterface = serviceInterface;

      provides = new AtomicBoolean(false);
   }

   public Class getServiceInterface() {
      return serviceInterface;
   }

   public void reset() {
      provides.set(false);
   }

   public void setProvides(boolean value) {
      provides.set(value);
   }

   public boolean provides() {
      return provides.get();
   }
}
