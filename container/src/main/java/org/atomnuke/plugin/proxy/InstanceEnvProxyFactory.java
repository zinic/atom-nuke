package org.atomnuke.plugin.proxy;

import org.atomnuke.plugin.InstanceEnvironment;

/**
 *
 * @author zinic
 */
public interface InstanceEnvProxyFactory {

   <T> T newProxy(Class<T> masqueradeClass, InstanceEnvironment<T> env);
}
