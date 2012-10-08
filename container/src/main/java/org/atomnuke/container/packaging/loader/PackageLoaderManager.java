package org.atomnuke.container.packaging.loader;

import java.util.Collection;
import org.atomnuke.container.packaging.PackageContext;
import org.atomnuke.service.Service;

/**
 *
 * @author zinic
 */
public interface PackageLoaderManager extends Service {

   Collection<PackageContext> loadedPackageContexts();
}
