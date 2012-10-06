package org.atomnuke.plugin;

/**
 *
 * An environment represents a context sandbox that surrounds a collection of
 * loaded instances. This sandbox may include support for context switching for
 * systems like class loaders.
 *
 * @author zinic
 */
public interface Environment {

   /**
    * Steps into the instance context. This allows the context to perform
    * context set up for any activity that may be performed on the contained
    * instance.
    *
    * @throws IllegalStateException should be thrown when this context is
    * stepped into more than once without stepping out of it.
    */
   void stepInto();

   /**
    * Steps out of the instance context. This allows the context to perform
    * context tear down after the contained instance has been interacted with.
    *
    * Stepping out of an environment may be invoked without explicitly calling
    * stepInto, making this call safe for use in finally statements.
    */
   void stepOut();
}
