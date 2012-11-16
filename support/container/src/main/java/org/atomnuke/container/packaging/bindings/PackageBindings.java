package org.atomnuke.container.packaging.bindings;

import org.atomnuke.container.packaging.bindings.lang.BindingLanguage;
import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.plugin.ReferenceInstantiationException;
import org.atomnuke.service.ServiceManager;

/**
 *
 * @author zinic
 */
public interface PackageBindings {

   void resolveServices(ServiceManager serviceManager) throws ReferenceInstantiationException;

   <T> InstanceContext<T> resolveReference(Class<T> castType, BindingLanguage type, String ref) throws ReferenceInstantiationException;
}
