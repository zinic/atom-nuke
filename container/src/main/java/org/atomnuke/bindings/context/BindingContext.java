package org.atomnuke.bindings.context;

import org.atomnuke.bindings.BindingInstantiationException;
import org.atomnuke.bindings.loader.Loader;
import org.atomnuke.bindings.lang.LanguageDescriptor;
import org.atomnuke.plugin.InstanceEnvironment;

/**
 *
 * @author zinic
 */
public interface BindingContext {

   LanguageDescriptor language();

   Loader loader();

   boolean hasRef(String ref);

   <T> InstanceEnvironment<T> instantiate(Class<T> interfaceType, String href) throws BindingInstantiationException;
}
