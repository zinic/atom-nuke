package org.atomnuke.bindings;

import java.util.LinkedList;
import java.util.List;
import org.atomnuke.bindings.context.BindingEnvironment;
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
public class PackageBindingsImpl implements PackageBindings {

   private final List<BindingEnvironment> availableBindings;

   public PackageBindingsImpl(List<BindingEnvironment> availableContexts) {
      this.availableBindings = new LinkedList<BindingEnvironment>(availableContexts);
   }

   @Override
   public InstanceContext<AtomEventlet> resolveEventlet(LanguageType type, String ref) throws BindingInstantiationException {
      for (BindingEnvironment bindingCtx : availableBindings) {
         if (bindingCtx.language().languageType() == type && bindingCtx.hasRef(ref)) {
            final AtomEventlet eventlet = bindingCtx.instantiate(AtomEventlet.class, ref);

            return new InstanceContextImpl<AtomEventlet>(bindingCtx.environment(), eventlet);
         }
      }

      return null;
   }

   @Override
   public InstanceContext<AtomListener> resolveListener(LanguageType type, String ref) throws BindingInstantiationException {
      for (BindingEnvironment bindingCtx : availableBindings) {
         if (bindingCtx.language().languageType() == type && bindingCtx.hasRef(ref)) {
            final AtomListener listener = bindingCtx.instantiate(AtomListener.class, ref);

            return new InstanceContextImpl<AtomListener>(bindingCtx.environment(), listener);
         }
      }

      return null;
   }

   @Override
   public InstanceContext<AtomSource> resolveSource(LanguageType type, String ref) throws BindingInstantiationException {
      for (BindingEnvironment bindingCtx : availableBindings) {
            if (bindingCtx.language().languageType() == type && bindingCtx.hasRef(ref)) {
               final AtomSource source = bindingCtx.instantiate(AtomSource.class, ref);

               return new InstanceContextImpl<AtomSource>(bindingCtx.environment(), source);
            }
         }

         return null;
      }
   }
