package org.atomnuke.service;

import org.atomnuke.plugin.InstanceContext;

/**
 *
 * @author zinic
 */
public class ServiceDescriptor implements Service {

   private final InstanceContext<ServiceLifeCycle> instanceEnvironment;
   private final String name;

   public ServiceDescriptor(String name, InstanceContext<ServiceLifeCycle> instanceEnvironment) {
      this.instanceEnvironment = instanceEnvironment;
      this.name = name;
   }

   @Override
   public String name() {
      return name;
   }

   @Override
   public InstanceContext<ServiceLifeCycle> instanceContext() {
      return instanceEnvironment;
   }
}
