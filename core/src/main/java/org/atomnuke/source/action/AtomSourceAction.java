package org.atomnuke.source.action;

/**
 *
 * @author zinic
 */
public interface AtomSourceAction<T> {

   boolean hasValue();

   T value();

   ActionType action();
}
