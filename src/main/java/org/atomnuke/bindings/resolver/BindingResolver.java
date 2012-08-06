package org.atomnuke.bindings.resolver;

import org.atomnuke.bindings.BindingInstantiationException;
import org.atomnuke.config.model.LanguageType;
import org.atomnuke.listener.AtomListener;
import org.atomnuke.listener.eps.eventlet.AtomEventlet;
import org.atomnuke.source.AtomSource;

/**
 *
 * @author zinic
 */
public interface BindingResolver {

   AtomEventlet resolveEventlet(LanguageType type, String ref) throws BindingInstantiationException;

   AtomListener resolveListener(LanguageType type, String ref) throws BindingInstantiationException;

   AtomSource resolveSource(LanguageType type, String ref) throws BindingInstantiationException;
}
