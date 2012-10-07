package org.atomnuke.plugin.proxy.japi;

import org.atomnuke.plugin.Environment;
import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.service.ServiceLifeCycle;

/**
 *
 * @author zinic
 */
public class ServiceProxy extends ObjectEnvironmentProxy {

   public ServiceProxy(InstanceContext<ServiceLifeCycle> svcInstanceContext) {
      this(svcInstanceContext.environment(), svcInstanceContext.instance());
   }

   public ServiceProxy(Environment environment, ServiceLifeCycle service) {
      super(environment, service.instance());
   }
}
