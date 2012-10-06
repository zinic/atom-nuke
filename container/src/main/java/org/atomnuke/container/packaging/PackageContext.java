package org.atomnuke.container.packaging;

import org.atomnuke.bindings.PackageBindings;

/**
 *
 * @author zinic
 */
public interface PackageContext {

   String name();

   PackageBindings packageBindings();
}
