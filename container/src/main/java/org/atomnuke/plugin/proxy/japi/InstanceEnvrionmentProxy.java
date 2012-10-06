package org.atomnuke.plugin.proxy.japi;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import org.atomnuke.plugin.InstanceContext;

/**
 *
 * @author zinic
 */
public class InstanceEnvrionmentProxy implements InvocationHandler {

   private final InstanceContext instanceCtx;

   public InstanceEnvrionmentProxy(InstanceContext instanceEnvironment) {
      this.instanceCtx = instanceEnvironment;
   }

   @Override
   public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      try {
         instanceCtx.environment().stepInto();

         return method.invoke(proxy, args);
      } finally {
         instanceCtx.environment().stepOut();
      }
   }
}
