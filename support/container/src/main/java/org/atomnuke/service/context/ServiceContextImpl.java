package org.atomnuke.service.context;

import org.atomnuke.service.ServiceContext;
import java.util.Map;
import org.atomnuke.NukeEnvironment;
import org.atomnuke.service.ServiceManager;

/**
 *
 * @author zinic
 */
public class ServiceContextImpl implements ServiceContext {

   private final Map<String, String> parameters;
   private final NukeEnvironment environment;
   private final ServiceManager manager;

   public ServiceContextImpl(NukeEnvironment environment, Map<String, String> parameters, ServiceManager manager) {
      this.environment = environment;
      this.parameters = parameters;
      this.manager = manager;
   }

   @Override
   public NukeEnvironment environment() {
      return environment;
   }

   @Override
   public ServiceManager services() {
      return manager;
   }

   @Override
   public Map<String, String> parameters() {
      return parameters;
   }
}
