package org.atomnuke.plugin.operation;

/**
 *
 * @author zinic
 */
public interface SimpleOperation<T> {

   void perform(T instance) throws OperationFailureException;
}
