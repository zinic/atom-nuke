package org.atomnuke.bindings;

import java.util.List;
import org.atomnuke.config.model.LanguageType;
import org.atomnuke.listener.AtomListener;
import org.atomnuke.listener.eps.eventlet.AtomEventlet;
import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.plugin.ReferenceInstantiationException;
import org.atomnuke.service.Service;
import org.atomnuke.source.AtomSource;

/**
 *
 * @author zinic
 */
public interface PackageBindings {

   List<InstanceContext<Service>> resolveServices() throws ReferenceInstantiationException;

   InstanceContext<AtomEventlet> resolveEventlet(LanguageType type, String ref) throws ReferenceInstantiationException;

   InstanceContext<AtomListener> resolveListener(LanguageType type, String ref) throws ReferenceInstantiationException;

   InstanceContext<AtomSource> resolveSource(LanguageType type, String ref) throws ReferenceInstantiationException;
}
