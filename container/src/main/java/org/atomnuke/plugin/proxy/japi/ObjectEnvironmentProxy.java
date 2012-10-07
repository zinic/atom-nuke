package org.atomnuke.plugin.proxy.japi;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import org.atomnuke.plugin.Environment;
import org.atomnuke.plugin.InstanceContext;

/**
 *
 * @author zinic
 */
public class ObjectEnvironmentProxy implements InvocationHandler {

   private final Environment environment;
   private final Object target;

   public ObjectEnvironmentProxy(InstanceContext instanceContext) {
      this(instanceContext.environment(), instanceContext.instance());
   }

   public ObjectEnvironmentProxy(Environment environment, Object target) {
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
