package org.atomnuke.plugin.proxy.japi;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import org.atomnuke.plugin.InstanceEnvironment;

/**
 *
 * @author zinic
 */
public class InstanceEnvrionmentProxy implements InvocationHandler {

   private final InstanceEnvironment instanceEnvironment;

   public InstanceEnvrionmentProxy(InstanceEnvironment instanceEnvironment) {
      this.instanceEnvironment = instanceEnvironment;
   }

   @Override
   public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      try {
         instanceEnvironment.stepInto();

         return method.invoke(proxy, args);
      } finally {
         instanceEnvironment.stepOut();
      }
   }
}
