package org.atomnuke.container.packaging.bindings;

import java.util.List;
import org.atomnuke.config.model.LanguageType;
import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.plugin.ReferenceInstantiationException;
import org.atomnuke.service.Service;

/**
 *
 * @author zinic
 */
public interface PackageBindings {

   List<InstanceContext<Service>> resolveServices() throws ReferenceInstantiationException;

   <T> InstanceContext<T> resolveReference(Class<T> castType, LanguageType type, String ref) throws ReferenceInstantiationException;
}
