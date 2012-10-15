package org.atomnuke.plugin.proxy.japi;

import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.service.Service;

/**
 *
 * @author zinic
 */
public class ServiceProxy extends InstanceEnvironmentProxy {

   public ServiceProxy(InstanceContext<Service> svc) {
      super(svc.environment(), svc.instance().instance());
   }
}
