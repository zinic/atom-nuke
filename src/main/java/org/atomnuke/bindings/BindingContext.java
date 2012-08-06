package org.atomnuke.bindings;

import org.atomnuke.bindings.loader.Loader;
import org.atomnuke.bindings.lang.LanguageDescriptor;

/**
 *
 * @author zinic
 */
public interface BindingContext {

   LanguageDescriptor language();

   Loader loader();
   
   boolean hasRef(String ref);

   <T> T instantiate(Class<T> interfaceType, String href) throws BindingInstantiationException;
}
