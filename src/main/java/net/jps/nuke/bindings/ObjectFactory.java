package net.jps.nuke.bindings;

/**
 *
 * @author zinic
 */
public interface ObjectFactory<T> {

    T createObject(String className) throws ClassNotFoundException;
}
