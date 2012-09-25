package org.atomnuke.bindings.resolver;

import java.util.List;
import org.atomnuke.bindings.context.BindingContext;
import org.atomnuke.bindings.BindingInstantiationException;
import org.atomnuke.plugin.InstanceEnvironment;
import org.atomnuke.config.model.LanguageType;
import org.atomnuke.listener.AtomListener;
import org.atomnuke.listener.eps.eventlet.AtomEventlet;
import org.atomnuke.source.AtomSource;

/**
 *
 * @author zinic
 */
public interface BindingResolver {

   List<BindingContext> registeredBindingContexts();

   InstanceEnvironment<AtomEventlet> resolveEventlet(LanguageType type, String ref) throws BindingInstantiationException;

   InstanceEnvironment<AtomListener> resolveListener(LanguageType type, String ref) throws BindingInstantiationException;

   InstanceEnvironment<AtomSource> resolveSource(LanguageType type, String ref) throws BindingInstantiationException;
}
