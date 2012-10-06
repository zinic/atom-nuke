package org.atomnuke.bindings.context;

import java.net.URI;
import org.atomnuke.bindings.BindingInstantiationException;
import org.atomnuke.bindings.BindingLoaderException;
import org.atomnuke.bindings.lang.LanguageDescriptor;
import org.atomnuke.plugin.Environment;

/**
 *
 * @author zinic
 */
public interface BindingContext {

   LanguageDescriptor language();

   void load(URI in) throws BindingLoaderException;

   boolean hasRef(String ref);

   Environment environment();

   <T> T instantiate(Class<T> interfaceType, String href) throws BindingInstantiationException;
}
