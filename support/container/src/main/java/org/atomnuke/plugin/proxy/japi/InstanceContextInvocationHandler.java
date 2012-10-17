package org.atomnuke.plugin.proxy.japi;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.plugin.operation.ComplexOperation;
import org.atomnuke.plugin.operation.OperationFailureException;
import org.atomnuke.plugin.proxy.ResultCatch;

/**
 *
 * @author zinic
 */
public class InstanceContextInvocationHandler implements InvocationHandler {

   private final InstanceContext context;

   public InstanceContextInvocationHandler(InstanceContext context) {
      this.context = context;
   }

   @Override
   public Object invoke(Object proxy, final Method method, final Object[] args) throws Throwable {
      final ResultCatch resultCatch = new ResultCatch();

      context.perform(new ComplexOperation<Object, ResultCatch>() {
         @Override
         public void perform(Object instance, ResultCatch argument) throws OperationFailureException {
            try {
               argument.setResult(method.invoke(instance, args));
            } catch (Exception ex) {
               throw new OperationFailureException(ex);
            }
         }
      }, resultCatch);

      return resultCatch.result();
   }
}
