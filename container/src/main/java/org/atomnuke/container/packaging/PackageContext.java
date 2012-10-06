package org.atomnuke.container.packaging;

import org.atomnuke.bindings.BindingContextManager;

/**
 *
 * @author zinic
 */
public interface PackageContext {

   String name();

   BindingContextManager bindingContextManager();
}
