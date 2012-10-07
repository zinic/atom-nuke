package org.atomnuke.plugin;

import java.util.List;
import org.atomnuke.service.Service;

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
    * Builds all discovered services for this environment.
    *
    * @return
    * @throws ReferenceInstantiationException
    */
   List<Service> instantiateServices() throws ReferenceInstantiationException;

   /**
    * Creates a new instance of a reference defined by the reference name string
    * parameter. This instance is bound to the environment and expects the
    * environment to be stepped into during use of the returned instance.
    *
    * @param <T>
    * @param interfaceType
    * @param referenceName
    * @return
    * @throws InstantiationException
    */
   <T> T instantiate(Class<T> interfaceType, String referenceName) throws ReferenceInstantiationException;

   /**
    * Checks to see if this environment has any instance bound to the reference
    * name string parameter.
    *
    * @param ref
    * @return
    */
   boolean hasReference(String referenceName);

   /**
    * Steps out of the instance context. This allows the context to perform
    * context tear down after the contained instance has been interacted with.
    *
    * Stepping out of an environment may be invoked without explicitly calling
    * stepInto, making this call safe for use in finally statements.
    */
   void stepOut();
}
