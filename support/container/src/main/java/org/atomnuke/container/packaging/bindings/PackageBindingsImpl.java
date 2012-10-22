package org.atomnuke.container.packaging.bindings;

import java.util.LinkedList;
import java.util.List;
import org.atomnuke.container.packaging.bindings.environment.BindingEnvironment;
import org.atomnuke.container.packaging.bindings.lang.BindingLanguage;
import org.atomnuke.plugin.Environment;
import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.plugin.InstanceContextImpl;
import org.atomnuke.plugin.ReferenceInstantiationException;
import org.atomnuke.service.Service;
import org.atomnuke.service.gc.ReclaimationHandler;
import org.atomnuke.util.lifecycle.Reclaimable;

/**
 *
 * @author zinic
 */
public class PackageBindingsImpl implements PackageBindings {

   private final List<BindingEnvironment> availableBindingEnvironments;
   private final ReclaimationHandler reclaimationHandler;

   public PackageBindingsImpl(ReclaimationHandler reclaimationHandler, List<BindingEnvironment> availableContexts) {
      this.availableBindingEnvironments = new LinkedList<BindingEnvironment>(availableContexts);
      this.reclaimationHandler = reclaimationHandler;
   }

   @Override
   public List<InstanceContext<Service>> resolveServices() throws ReferenceInstantiationException {
      final List<InstanceContext<Service>> services = new LinkedList<InstanceContext<Service>>();

      for (BindingEnvironment bindingEnvironment : availableBindingEnvironments) {
         final Environment env = bindingEnvironment.environment();

         for (Service discoveredService : env.services()) {
            reclaimationHandler.watch(new InstanceContextImpl<Reclaimable>(env, discoveredService));

            services.add(new InstanceContextImpl<Service>(env, discoveredService));
         }
      }

      return services;
   }

   @Override
   public <T> InstanceContext<T> resolveReference(Class<T> type, BindingLanguage language, String ref) throws ReferenceInstantiationException {
      for (BindingEnvironment bindingCtx : availableBindingEnvironments) {
         final Environment env = bindingCtx.environment();

         if (bindingCtx.language().language() == language && env.hasReference(ref)) {
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
