package org.atomnuke.plugin.proxy;

import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.service.Service;

/**
 *
 * @author zinic
 */
public interface InstanceEnvProxyFactory {

   <T> T newInstanceContextProxy(Class<T> masqueradeClass, InstanceContext<T> env);

   <T> T newServiceProxy(Class<T> masqueradeClass, InstanceContext<Service> svc);
}
