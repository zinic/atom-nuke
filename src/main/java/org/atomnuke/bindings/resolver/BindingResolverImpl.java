package org.atomnuke.bindings.resolver;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.atomnuke.bindings.BindingContext;
import org.atomnuke.bindings.BindingInstantiationException;
import org.atomnuke.bindings.classpath.ClasspathBindingContext;
import org.atomnuke.bindings.jython.PythonInterpreterContext;
import org.atomnuke.config.model.LanguageType;
import org.atomnuke.listener.AtomListener;
import org.atomnuke.listener.eps.eventlet.AtomEventlet;
import org.atomnuke.source.AtomSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class BindingResolverImpl implements BindingResolver {

   private static final Logger LOG = LoggerFactory.getLogger(BindingResolverImpl.class);
   private static final BindingResolver DEFAULT_RESOLVER = new BindingResolverImpl(new ClasspathBindingContext(), new PythonInterpreterContext(LOG.isDebugEnabled()));

   public static BindingResolver defaultResolver() {
      return DEFAULT_RESOLVER;
   }
   private final List<BindingContext> contexts;

   public BindingResolverImpl() {
      contexts = new LinkedList<BindingContext>();
   }

   public BindingResolverImpl(BindingContext... contextArray) {
      contexts = Arrays.asList(contextArray);
   }

   @Override
   public List<BindingContext> registeredBindingContexts() {
      return contexts;
   }

   @Override
   public AtomListener resolveListener(LanguageType type, String ref) throws BindingInstantiationException {
      for (BindingContext context : contexts) {
         if (context.language().languageType() == type && context.hasRef(ref)) {
            return context.instantiate(AtomListener.class, ref);
         }
      }

      return null;
   }

   @Override
   public AtomSource resolveSource(LanguageType type, String ref) throws BindingInstantiationException {
      for (BindingContext context : contexts) {
         if (context.language().languageType() == type && context.hasRef(ref)) {
            return context.instantiate(AtomSource.class, ref);
         }
      }

      return null;
   }

   @Override
   public AtomEventlet resolveEventlet(LanguageType type, String ref) throws BindingInstantiationException {
      for (BindingContext context : contexts) {
         if (context.language().languageType() == type && context.hasRef(ref)) {
            return context.instantiate(AtomEventlet.class, ref);
         }
      }

      return null;
   }
}
