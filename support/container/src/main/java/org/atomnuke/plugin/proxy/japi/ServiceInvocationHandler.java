package org.atomnuke.plugin.proxy.japi;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.plugin.operation.ComplexOperation;
import org.atomnuke.plugin.operation.OperationFailureException;
import org.atomnuke.util.result.ResultCatchImpl;
import org.atomnuke.service.Service;

/**
 *
 * @author zinic
 */
public class ServiceInvocationHandler implements InvocationHandler {

   private final InstanceContext<Service> context;

   public ServiceInvocationHandler(InstanceContext<Service> context) {
      this.context = context;
   }

   @Override
   public Object invoke(Object proxy, final Method method, final Object[] args) throws Throwable {
      final ResultCatchImpl resultCatch = new ResultCatchImpl();

      context.perform(new ComplexOperation<Service, ResultCatchImpl>() {
         @Override
         public void perform(Service service, ResultCatchImpl argument) throws OperationFailureException {
            try {
               argument.setResult(method.invoke(service.instance(), args));
            } catch (InvocationTargetException ex) {
               throw new OperationFailureException(ex.getTargetException());
            } catch(Exception ex) {
               throw new OperationFailureException(ex);
            }
         }
      }, resultCatch);

      return resultCatch.result();
   }
}
