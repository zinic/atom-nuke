package org.atomnuke.context;

/**
 * An instance context represents a context sandbox that surrounds a loaded
 * instance of type T. This sandbox may include support for context switching
 * for systems like class loaders.
 *
 * @author zinic
 */
public interface InstanceContext<T> {

   /**
    * Retrieves the instance this context surrounds.
    *
    * @return the instance the context surrounds.
    */
   T getInstance();

   /**
    * Steps out of the instance context. This allows the context to perform
    * context tear down after the contained instance has been interacted with.
    */
   void stepOut();

   /**
    * Steps into the instance context. This allows the context to perform
    * context set up for any activity that may be performed on the contained
    * instance.
    *
    * @throws IllegalStateException should be thrown when this context is
    * stepped into more than once without stepping out of it.
    */
   void stepInto();
}
