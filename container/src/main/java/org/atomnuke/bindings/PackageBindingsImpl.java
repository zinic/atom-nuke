package org.atomnuke.bindings;

import java.util.LinkedList;
import java.util.List;
import org.atomnuke.bindings.context.BindingEnvironment;
import org.atomnuke.config.model.LanguageType;
import org.atomnuke.listener.AtomListener;
import org.atomnuke.listener.eps.eventlet.AtomEventlet;
import org.atomnuke.plugin.Environment;
import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.plugin.InstanceContextImpl;
import org.atomnuke.plugin.ReferenceInstantiationException;
import org.atomnuke.service.Service;
import org.atomnuke.source.AtomSource;

/**
 *
 * @author zinic
 */
public class PackageBindingsImpl implements PackageBindings {

   private final List<BindingEnvironment> availableBindingEnvironments;

   public PackageBindingsImpl(List<BindingEnvironment> availableContexts) {
      this.availableBindingEnvironments = new LinkedList<BindingEnvironment>(availableContexts);
   }

   @Override
   public List<InstanceContext<Service>> resolveServices() throws ReferenceInstantiationException {
      final List<InstanceContext<Service>> services = new LinkedList<InstanceContext<Service>>();

      for (BindingEnvironment bindingEnvironment : availableBindingEnvironments) {
         final Environment env = bindingEnvironment.environment();

         for (Service discoveredService : env.services()) {
            services.add(new InstanceContextImpl<Service>(env, discoveredService));
         }
      }

      return services;
   }

   @Override
   public InstanceContext<AtomEventlet> resolveEventlet(LanguageType type, String ref) throws ReferenceInstantiationException {
      return resolve(AtomEventlet.class, type, ref);
   }

   @Override
   public InstanceContext<AtomListener> resolveListener(LanguageType type, String ref) throws ReferenceInstantiationException {
      return resolve(AtomListener.class, type, ref);
   }

   @Override
   public InstanceContext<AtomSource> resolveSource(LanguageType type, String ref) throws ReferenceInstantiationException {
      return resolve(AtomSource.class, type, ref);
   }

   private <T> InstanceContext<T> resolve(Class<T> type, LanguageType language, String ref) throws ReferenceInstantiationException {
      for (BindingEnvironment bindingCtx : availableBindingEnvironments) {
         final Environment env = bindingCtx.environment();

         if (bindingCtx.language().languageType() == language && env.hasReference(ref)) {
            try {
               return new InstanceContextImpl<T>(env, env.instantiate(type, ref));
            } catch (ReferenceInstantiationException rie) {
               throw new ReferenceInstantiationException(ref, rie);
            }
         }
      }

      return null;
   }
}
