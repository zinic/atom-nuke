package org.atomnuke.bindings;

/**
 *
 * @author zinic
 */
public interface BindingContext {

   LanguageDescriptor language();

   Loader loader();

   <T> T instantiate(Class<T> interfaceType, String href) throws BindingInstantiationException;
}
