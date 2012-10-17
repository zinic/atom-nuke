package org.atomnuke.plugin.proxy.japi;

import java.lang.reflect.Proxy;
import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.plugin.proxy.InstanceEnvProxyFactory;
import org.atomnuke.service.Service;

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
   public <T> T newProxy(Class<T> masqueradeClass, InstanceContext instanceContext) {
      return (T) Proxy.newProxyInstance(
              Thread.currentThread().getContextClassLoader(), new Class[]{masqueradeClass}, new InstanceContextInvocationHandler(instanceContext));
   }

   @Override
   public <T> T newServiceProxy(Class<T> masqueradeClass, InstanceContext<Service> instanceContext) {
      return (T) Proxy.newProxyInstance(
              Thread.currentThread().getContextClassLoader(), new Class[]{masqueradeClass}, new ServiceInvocationHandler(instanceContext));
   }
}
