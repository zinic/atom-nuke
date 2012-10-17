package org.atomnuke.plugin.operation;

/**
 *
 * @author zinic
 */
public interface ComplexOperation<T, A> {

   void perform(T instance, A argument) throws OperationFailureException;
}
