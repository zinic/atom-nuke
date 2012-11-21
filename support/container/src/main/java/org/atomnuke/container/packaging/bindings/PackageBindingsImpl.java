package org.atomnuke.container.packaging.bindings;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.atomnuke.container.packaging.bindings.environment.BindingEnvironment;
import org.atomnuke.container.packaging.bindings.lang.BindingLanguage;
import org.atomnuke.plugin.Environment;
import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.plugin.ReferenceInstantiationException;
import org.atomnuke.plugin.context.InstanceContextImpl;
import org.atomnuke.service.Service;
import org.atomnuke.service.ServiceAlreadyRegisteredException;
import org.atomnuke.service.ServiceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class PackageBindingsImpl implements PackageBindings {

   private static final Logger LOG = LoggerFactory.getLogger(PackageBindingsImpl.class);

   private final List<BindingEnvironment> availableBindingEnvironments;

   public PackageBindingsImpl(List<BindingEnvironment> availableContexts) {
      this.availableBindingEnvironments = new LinkedList<BindingEnvironment>(availableContexts);
   }

   @Override
   public void resolveServices(ServiceManager serviceManager) throws ReferenceInstantiationException {
      final Map<String, InstanceContext<Service>> services = new HashMap<String, InstanceContext<Service>>();

      for (BindingEnvironment bindingEnvironment : availableBindingEnvironments) {
         final Environment env = bindingEnvironment.environment();

         for (Service discoveredService : env.services()) {
            if (!services.containsKey(discoveredService.name())) {
               services.put(discoveredService.name(), new InstanceContextImpl<Service>(env, discoveredService));
            }
         }
      }

      for (Map.Entry<String, InstanceContext<Service>> serviceEntry : services.entrySet()) {
         if (!serviceManager.serviceRegistered(serviceEntry.getKey())) {
            try {
               serviceManager.submit(serviceEntry.getValue());
            } catch (ServiceAlreadyRegisteredException sare) {
               LOG.debug("Duplicate service for name: " + serviceEntry.getKey() + " found - this version will not be loaded!", sare);
            }
         }
      }
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
