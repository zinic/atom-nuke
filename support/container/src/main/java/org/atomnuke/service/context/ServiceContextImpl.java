package org.atomnuke.service.context;

import org.atomnuke.service.ServiceContext;
import java.util.Map;
import org.atomnuke.NukeEnvironment;
import org.atomnuke.service.ServiceManager;
import org.atomnuke.service.introspection.ServicesInterrogator;

/**
 *
 * @author zinic
 */
public class ServiceContextImpl implements ServiceContext {

   private final ServicesInterrogator interrogator;
   private final Map<String, String> parameters;
   private final NukeEnvironment environment;
   private final ServiceManager manager;

   public ServiceContextImpl(ServicesInterrogator interrogator, Map<String, String> parameters, NukeEnvironment environment, ServiceManager manager) {
      this.interrogator = interrogator;
      this.parameters = parameters;
      this.environment = environment;
      this.manager = manager;
   }

   @Override
   public NukeEnvironment environment() {
      return environment;
   }

   @Override
   public ServiceManager serviceManager() {
      return manager;
   }

   @Override
   public ServicesInterrogator services() {
      return interrogator;
   }

   @Override
   public Map<String, String> parameters() {
      return parameters;
   }
}
