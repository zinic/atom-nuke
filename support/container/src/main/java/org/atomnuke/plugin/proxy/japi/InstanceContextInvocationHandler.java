package org.atomnuke.plugin.proxy.japi;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.plugin.operation.ComplexOperation;
import org.atomnuke.plugin.operation.OperationFailureException;
import org.atomnuke.util.result.ResultCatchImpl;

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
   public Object invoke(Object proxy, final Method method, final Object[] args) throws OperationFailureException {
      final ResultCatchImpl resultCatch = new ResultCatchImpl();

      context.perform(new ComplexOperation<Object, ResultCatchImpl>() {
         @Override
         public void perform(Object instance, ResultCatchImpl argument) throws OperationFailureException {
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
