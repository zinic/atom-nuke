package org.atomnuke.bindings.resolver;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.atomnuke.NukeEnv;
import org.atomnuke.bindings.context.BindingContext;
import org.atomnuke.bindings.BindingInstantiationException;
import org.atomnuke.bindings.ear.EarBindingContext;
import org.atomnuke.bindings.jython.PythonInterpreterContext;
import org.atomnuke.bindings.rhinojs.RhinoInterpreterContext;
import org.atomnuke.plugin.InstanceEnvironment;
import org.atomnuke.config.model.LanguageType;
import org.atomnuke.listener.AtomListener;
import org.atomnuke.listener.eps.eventlet.AtomEventlet;
import org.atomnuke.service.ServiceManager;
import org.atomnuke.source.AtomSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class BindingResolverImpl implements BindingResolver {

   private static final Logger LOG = LoggerFactory.getLogger(BindingResolverImpl.class);

   public static BindingResolver defaultResolver(ServiceManager serviceManager) {
      final BindingResolver DEFAULT_RESOLVER = new BindingResolverImpl(
              new EarBindingContext(serviceManager, new File(NukeEnv.NUKE_DEPLOY)),
              new PythonInterpreterContext(LOG.isDebugEnabled()),
              RhinoInterpreterContext.newInstance());

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
   public InstanceEnvironment<AtomListener> resolveListener(LanguageType type, String ref) throws BindingInstantiationException {
      for (BindingContext context : contexts) {
         if (context.language().languageType() == type && context.hasRef(ref)) {
            return context.instantiate(AtomListener.class, ref);
         }
      }

      return null;
   }

   @Override
   public InstanceEnvironment<AtomSource> resolveSource(LanguageType type, String ref) throws BindingInstantiationException {
      for (BindingContext context : contexts) {
         if (context.language().languageType() == type && context.hasRef(ref)) {
            return context.instantiate(AtomSource.class, ref);
         }
      }

      return null;
   }

   @Override
   public InstanceEnvironment<AtomEventlet> resolveEventlet(LanguageType type, String ref) throws BindingInstantiationException {
      for (BindingContext context : contexts) {
         if (context.language().languageType() == type && context.hasRef(ref)) {
            return context.instantiate(AtomEventlet.class, ref);
         }
      }

      return null;
   }
}
