package org.atomnuke.container.packaging.loader.impl;

import org.atomnuke.container.packaging.loader.PackageLoader;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.atomnuke.container.packaging.bindings.BindingEnvironmentFactory;
import org.atomnuke.container.packaging.bindings.PackageLoadingException;
import org.atomnuke.container.packaging.PackageContext;
import org.atomnuke.container.packaging.PackageContextImpl;
import org.atomnuke.container.packaging.bindings.PackageBindingsImpl;
import org.atomnuke.container.packaging.bindings.environment.BindingEnvironment;
import org.atomnuke.container.packaging.resource.Resource;
import org.atomnuke.container.packaging.resource.ResourceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class BindingAwarePackageLoader implements PackageLoader {

   private static final Logger LOG = LoggerFactory.getLogger(BindingAwarePackageLoader.class);

   private final Map<String, PackageContext> loadedPackages;
   private final BindingEnvironmentFactory bindingEnvFactory;

   public BindingAwarePackageLoader(BindingEnvironmentFactory bindingEnvFactory) {
      this.bindingEnvFactory = bindingEnvFactory;

      loadedPackages = new HashMap<String, PackageContext>();
   }

   @Override
   public synchronized void load(String name, ResourceManager deployedPackage) throws PackageLoadingException {
      final List<BindingEnvironment> bindingEnvironments = bindingEnvFactory.newEnviornment(deployedPackage);
      final PackageContext newPackageContext = new PackageContextImpl(new PackageBindingsImpl(bindingEnvironments), name);

      // Read through all of the resources
      for (Resource resource : deployedPackage.resources()) {
         final BindingEnvironment env = getBindingEnvironment(bindingEnvironments, resource);

         if (env != null) {
            try {
                  env.load(resource);
            } catch (PackageLoadingException ble) {
               LOG.error("Failed to load package: " + name + ". Reason: " + ble.getMessage(), ble);
            }
         }
      }

      // Register the new package
      loadedPackages.put(name, newPackageContext);
   }

   private static BindingEnvironment getBindingEnvironment(List<BindingEnvironment> bindingEnvironments, Resource resource) {
      for (BindingEnvironment env : bindingEnvironments) {
         for (String extension : env.language().fileExtensions()) {
            if (resource.relativePath().endsWith(extension)) {
               return env;
            }
         }
      }

      return null;
   }

   @Override
   public Collection<PackageContext> packageContexts() {
      return loadedPackages.values();
   }
}
