package org.atomnuke.service;

import org.atomnuke.plugin.InstanceEnvironment;

/**
 *
 * @author zinic
 */
public class ServiceDescriptor implements Service {

   private final InstanceEnvironment<ServiceLifeCycle> instanceEnvironment;
   private final String name;

   public ServiceDescriptor(String name, InstanceEnvironment<ServiceLifeCycle> instanceEnvironment) {
      this.instanceEnvironment = instanceEnvironment;
      this.name = name;
   }

   @Override
   public String name() {
      return name;
   }

   @Override
   public InstanceEnvironment<ServiceLifeCycle> instanceEnvironment() {
      return instanceEnvironment;
   }
}
