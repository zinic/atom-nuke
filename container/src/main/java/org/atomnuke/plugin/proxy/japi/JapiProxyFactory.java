package org.atomnuke.plugin.proxy.japi;

import java.lang.reflect.Proxy;
import org.atomnuke.plugin.InstanceEnvironment;
import org.atomnuke.plugin.proxy.InstanceEnvProxyFactory;

/**
 *
 * @author zinic
 */
public class JapiProxyFactory implements InstanceEnvProxyFactory {

   private static final InstanceEnvProxyFactory STATIC_INSTANCE = new JapiProxyFactory();

   public static InstanceEnvProxyFactory getInstance() {
      return STATIC_INSTANCE;
   }

   @Override
   public <T> T newProxy(Class<T> masqueradeClass, InstanceEnvironment<T> env) {
      return (T) Proxy.newProxyInstance(
              Thread.currentThread().getContextClassLoader(), new Class[]{masqueradeClass}, new InstanceEnvrionmentProxy(env));
   }
}
