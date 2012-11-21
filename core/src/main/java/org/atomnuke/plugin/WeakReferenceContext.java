package org.atomnuke.plugin;

import java.lang.ref.WeakReference;
import org.atomnuke.plugin.operation.ComplexOperation;
import org.atomnuke.plugin.operation.OperationFailureException;
import org.atomnuke.plugin.operation.SimpleOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class WeakReferenceContext<T> implements InstanceContext<T> {

   private static final Logger LOG = LoggerFactory.getLogger(Logger.class);

   private final WeakReference<Environment> environmentRef;
   private final WeakReference<T> instanceRef;

   public WeakReferenceContext(Environment environment, T instance) {
      this.environmentRef = new WeakReference<Environment>(environment);
      this.instanceRef = new WeakReference<T>(instance);
   }

   @Override
   public Class<T> instanceClass() {
      final T instance = instanceRef.get();
      return instance != null ? (Class<T>) instance.getClass() : null;
   }

   @Override
   public T instance() {
      return instanceRef.get();
   }

   @Override
   public void perform(SimpleOperation<T> requestedOperation) {
      final Environment environment = environmentRef.get();
      final T instance = instanceRef.get();

      if (environment == null || instance == null) {
         LOG.debug("Environment and or instance was null. This context will be garbage collected soon. Refusing to perform operation: " + requestedOperation);
         return;
      }

      try {
         environment.stepInto();
         requestedOperation.perform(instance);
      } catch (OperationFailureException ofe) {
         LOG.error("Failed to perform requested operation within an instance context. Failure: " + ofe.getMessage(), ofe);
      } finally {
         environment.stepOut();
      }
   }

   @Override
   public <A> void perform(ComplexOperation<T, A> requestedOperation, A argument) {
      final Environment environment = environmentRef.get();
      final T instance = instanceRef.get();

      if (environment == null || instance == null) {
         LOG.debug("Environment and or instance was null. This context will be garbage collected soon. Refusing to perform operation: " + requestedOperation);
         return;
      }

      try {
         environment.stepInto();
         requestedOperation.perform(instance, argument);
      } catch (OperationFailureException ofe) {
         LOG.error("Failed to perform requested operation within an instance context. Failure: " + ofe.getMessage(), ofe);
      } finally {
         environment.stepOut();
      }
   }
}