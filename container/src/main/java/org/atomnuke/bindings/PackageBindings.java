package org.atomnuke.bindings;

import java.util.List;
import org.atomnuke.config.model.LanguageType;
import org.atomnuke.listener.AtomListener;
import org.atomnuke.listener.eps.eventlet.AtomEventlet;
import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.service.Service;
import org.atomnuke.source.AtomSource;

/**
 *
 * @author zinic
 */
public interface PackageBindings {

   List<Service> resolveServices() throws BindingInstantiationException;

   InstanceContext<AtomEventlet> resolveEventlet(LanguageType type, String ref) throws BindingInstantiationException;

   InstanceContext<AtomListener> resolveListener(LanguageType type, String ref) throws BindingInstantiationException;

   InstanceContext<AtomSource> resolveSource(LanguageType type, String ref) throws BindingInstantiationException;
}
