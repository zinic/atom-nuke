package org.atomnuke.plugin;

import org.atomnuke.plugin.operation.ComplexOperation;
import org.atomnuke.plugin.operation.OperationFailureException;
import org.atomnuke.plugin.operation.SimpleOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class InstanceContextImpl<T> implements InstanceContext<T> {

   private static final Logger LOG = LoggerFactory.getLogger(Logger.class);
   private final Environment environment;
   private final T instance;

   public InstanceContextImpl(Environment environment, T instance) {
      this.environment = environment;
      this.instance = instance;
   }

   @Override
   public T instance() {
      return instance;
   }

   @Override
   public void perform(SimpleOperation<T> requestedOperation) {
      try {
         environment.stepInto();
         requestedOperation.perform(instance());
      } catch (OperationFailureException ofe) {
         LOG.error("Failed to perform requested operation within an instance context. Failure: " + ofe.getMessage(), ofe);
      } finally {
         environment.stepOut();
      }
   }

   @Override
   public <A> void perform(ComplexOperation<T, A> requestedOperation, A argument) {
      try {
         environment.stepInto();
         requestedOperation.perform(instance(), argument);
      } catch (OperationFailureException ofe) {
         LOG.error("Failed to perform requested operation within an instance context. Failure: " + ofe.getMessage(), ofe);
      } finally {
         environment.stepOut();
      }
   }
}
