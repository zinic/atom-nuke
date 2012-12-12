package org.atomnuke.plugin.proxy.japi;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
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

   private final Map<String, Method> actualMethodLookaside;
   private final InstanceContext<Service> context;

   public ServiceInvocationHandler(InstanceContext<Service> context) {
      this.context = context;

      actualMethodLookaside = new HashMap<String, Method>();
   }

   private synchronized Method getActualMethod(final Method searchFor) throws NoSuchMethodException {
      Method cachedMethod = actualMethodLookaside.get(searchFor.getName());

      if (cachedMethod == null) {
         cachedMethod = context.instance().instance().getClass().getMethod(searchFor.getName(), searchFor.getParameterTypes());
      }

      return cachedMethod;
   }

   @Override
   public Object invoke(Object proxy, final Method method, final Object[] args) throws NoSuchMethodException, OperationFailureException { 
      final ResultCatchImpl resultCatch = new ResultCatchImpl();

      // TODO: Failure to find the actual method should result in a failure...
      final Method actualMethod = getActualMethod(method);

      context.perform(new ComplexOperation<Service, ResultCatchImpl>() {
         @Override
         public void perform(Service service, ResultCatchImpl argument) throws OperationFailureException {
            try {
               argument.setResult(actualMethod.invoke(service.instance(), args));
            } catch (InvocationTargetException ex) {
               throw new OperationFailureException(ex.getTargetException());
            } catch (Exception ex) {
               throw new OperationFailureException(ex);
            }
         }
      }, resultCatch);

      return resultCatch.result();
   }
}
