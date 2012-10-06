package org.atomnuke.bindings;

import java.util.LinkedList;
import java.util.List;
import org.atomnuke.bindings.context.BindingContext;
import org.atomnuke.config.model.LanguageType;
import org.atomnuke.listener.AtomListener;
import org.atomnuke.listener.eps.eventlet.AtomEventlet;
import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.plugin.InstanceContextImpl;
import org.atomnuke.source.AtomSource;

/**
 *
 * @author zinic
 */
public class BindingContextManagerImpl implements BindingContextManager {

   private final List<BindingContext> availableContexts;

   public BindingContextManagerImpl(List<BindingContext> availableContexts) {
      this.availableContexts = new LinkedList<BindingContext>(availableContexts);
   }

   @Override
   public List<BindingContext> availableContexts() {
      return availableContexts;
   }

   @Override
   public InstanceContext<AtomEventlet> resolveEventlet(LanguageType type, String ref) throws BindingInstantiationException {
      for (BindingContext context : availableContexts()) {
         if (context.language().languageType() == type && context.hasRef(ref)) {
            final AtomEventlet eventlet = context.instantiate(AtomEventlet.class, ref);

            return new InstanceContextImpl<AtomEventlet>(context.environment(), eventlet);
         }
      }

      return null;
   }

   @Override
   public InstanceContext<AtomListener> resolveListener(LanguageType type, String ref) throws BindingInstantiationException {
      for (BindingContext context : availableContexts()) {
         if (context.language().languageType() == type && context.hasRef(ref)) {
            final AtomListener listener = context.instantiate(AtomListener.class, ref);

            return new InstanceContextImpl<AtomListener>(context.environment(), listener);
         }
      }

      return null;
   }

   @Override
   public InstanceContext<AtomSource> resolveSource(LanguageType type, String ref) throws BindingInstantiationException {
      for (BindingContext context : availableContexts()) {
         if (context.language().languageType() == type && context.hasRef(ref)) {
            final AtomSource source = context.instantiate(AtomSource.class, ref);

            return new InstanceContextImpl<AtomSource>(context.environment(), source);
         }
      }

      return null;
   }
}
