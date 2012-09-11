package org.atomnuke.bindings.resolver;

import java.util.List;
import org.atomnuke.bindings.BindingContext;
import org.atomnuke.bindings.BindingInstantiationException;
import org.atomnuke.context.InstanceContext;
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

   InstanceContext<AtomEventlet> resolveEventlet(LanguageType type, String ref) throws BindingInstantiationException;

   InstanceContext<AtomListener> resolveListener(LanguageType type, String ref) throws BindingInstantiationException;

   InstanceContext<AtomSource> resolveSource(LanguageType type, String ref) throws BindingInstantiationException;
}
