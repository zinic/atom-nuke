package org.atomnuke.container.packaging.bindings.environment;

import org.atomnuke.container.packaging.bindings.PackageLoaderException;
import org.atomnuke.container.packaging.bindings.LanguageDescriptor;
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
