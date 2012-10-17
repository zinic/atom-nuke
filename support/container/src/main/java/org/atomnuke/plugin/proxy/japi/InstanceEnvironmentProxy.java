package org.atomnuke.plugin.proxy.japi;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import org.atomnuke.plugin.Environment;

/**
 *
 * @author zinic
 */
public class InstanceEnvironmentProxy implements InvocationHandler {

   private final Environment environment;
   private final Object target;

   public InstanceEnvironmentProxy(Environment environment, Object target) {
      this.environment = environment;
      this.target = target;
   }

   @Override
   public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      try {
         environment.stepInto();
         return method.invoke(target, args);
      } finally {
         environment.stepOut();
      }
   }
}
