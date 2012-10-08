package org.atomnuke.bindings.loader;

import java.util.Collection;
import org.atomnuke.container.packaging.PackageContext;
import org.atomnuke.service.ServiceLifeCycle;

/**
 *
 * @author zinic
 */
public interface PackageLoaderManager extends ServiceLifeCycle {

   Collection<PackageContext> loadedPackageContexts();
}
