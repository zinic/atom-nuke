package org.atomnuke.plugin;

import org.atomnuke.plugin.operation.ComplexOperation;
import org.atomnuke.plugin.operation.OperationFailureException;
import org.atomnuke.plugin.operation.SimpleOperation;

/**
 * An instance context represents a context sandbox that surrounds a loaded
 * instance of type T.
 *
 * @author zinic
 */
public interface InstanceContext<T> {

   Class<T> instanceClass();

   T instance();

   void perform(SimpleOperation<T> requestedOperation) throws OperationFailureException;

   <A> void perform(ComplexOperation<T, A> requestedOperation, A argument) throws OperationFailureException;
}
