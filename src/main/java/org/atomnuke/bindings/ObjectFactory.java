package org.atomnuke.bindings;

/**
 *
 * @author zinic
 */
public interface ObjectFactory<T> {

    T createObject(String className) throws ClassNotFoundException;
}
