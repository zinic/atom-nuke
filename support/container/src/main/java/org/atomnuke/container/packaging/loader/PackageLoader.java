package org.atomnuke.container.packaging.loader;

import java.util.Collection;
import org.atomnuke.container.packaging.DeployedPackage;
import org.atomnuke.container.packaging.PackageContext;
import org.atomnuke.container.packaging.bindings.PackageLoadingException;
import org.atomnuke.container.packaging.resource.ResourceManager;

/**
 *
 * @author zinic
 */
public interface PackageLoader {
   
   void load(DeployedPackage deployedPackage) throws PackageLoadingException;

   Collection<PackageContext> packageContexts();
}
