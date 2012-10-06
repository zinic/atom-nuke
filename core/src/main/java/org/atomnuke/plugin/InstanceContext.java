package org.atomnuke.plugin;

/**
 * An instance context represents a context sandbox that surrounds a loaded
 * instance of type T.
 *
 * @author zinic
 */
public interface InstanceContext<T> {

   /**
    * Retrieves the instance this context surrounds.
    *
    * @return the instance the context surrounds.
    */
   T instance();

   Environment environment();
}
