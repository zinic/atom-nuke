package org.atomnuke.plugin.proxy;

import org.atomnuke.plugin.InstanceContext;

/**
 *
 * @author zinic
 */
public interface InstanceEnvProxyFactory {

   <T> T newProxy(Class<T> masqueradeClass, InstanceContext<T> env);
}
