package org.atomnuke.container.packaging;

/**
 *
 * @author zinic
 */
public interface PackageLoader {

   PackageContext load(DeployedPackage deployedPackage);
}
