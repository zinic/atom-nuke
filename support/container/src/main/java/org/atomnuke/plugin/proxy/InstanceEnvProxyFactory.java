package org.atomnuke.plugin.proxy;

import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.service.Service;

/**
 *
 * @author zinic
 */
public interface InstanceEnvProxyFactory {

   <T> T newProxy(Class<T> masqueradeClass, InstanceContext instanceContext);

   <T> T newServiceProxy(Class<T> masqueradeClass, InstanceContext<Service> serviceContext);
}
