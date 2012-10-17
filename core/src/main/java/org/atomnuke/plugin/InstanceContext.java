package org.atomnuke.plugin;

import org.atomnuke.plugin.operation.ComplexOperation;
import org.atomnuke.plugin.operation.SimpleOperation;

/**
 * An instance context represents a context sandbox that surrounds a loaded
 * instance of type T.
 *
 * @author zinic
 */
public interface InstanceContext<T> {

   T instance();

   void perform(SimpleOperation<T> requestedOperation);

   <A> void perform(ComplexOperation<T, A> requestedOperation, A argument);
}
