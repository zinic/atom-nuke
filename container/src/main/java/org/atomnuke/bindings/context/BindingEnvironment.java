package org.atomnuke.bindings.context;

import org.atomnuke.bindings.PackageLoaderException;
import org.atomnuke.bindings.lang.LanguageDescriptor;
import org.atomnuke.container.packaging.resource.Resource;
import org.atomnuke.plugin.Environment;

/**
 *
 * @author zinic
 */
public interface BindingEnvironment {

   LanguageDescriptor language();

   Environment environment();

   void load(Resource resource) throws PackageLoaderException;
}
