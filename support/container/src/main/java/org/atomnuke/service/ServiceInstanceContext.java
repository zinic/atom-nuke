package org.atomnuke.service;

import org.atomnuke.plugin.Environment;

/**
 *
 * @author zinic
 */
public class ServiceInstanceContext {

   private final Environment environment;
   private final Service service;

   public ServiceInstanceContext(Environment environment, Service service) {
      this.environment = environment;
      this.service = service;
   }

   public Environment environment() {
      return environment;
   }

   public Service service() {
      return service;
   }
}
