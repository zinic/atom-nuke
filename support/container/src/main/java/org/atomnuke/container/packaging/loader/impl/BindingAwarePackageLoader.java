package org.atomnuke.container.packaging.loader.impl;

import org.atomnuke.container.packaging.loader.PackageLoader;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.atomnuke.container.packaging.bindings.BindingEnvironmentFactory;
import org.atomnuke.container.packaging.bindings.PackageLoadingException;
import org.atomnuke.container.packaging.DeployedPackage;
import org.atomnuke.container.packaging.PackageContext;
import org.atomnuke.container.packaging.PackageContextImpl;
import org.atomnuke.container.packaging.bindings.PackageBindingsImpl;
import org.atomnuke.container.packaging.bindings.environment.BindingEnvironment;
import org.atomnuke.container.packaging.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class BindingAwarePackageLoader implements PackageLoader {

   private static final Logger LOG = LoggerFactory.getLogger(BindingAwarePackageLoader.class);

   private final Map<DeployedPackage, PackageContext> loadedPackages;
   private final BindingEnvironmentFactory bindingEnvFactory;

   public BindingAwarePackageLoader(BindingEnvironmentFactory bindingEnvFactory) {
      this.bindingEnvFactory = bindingEnvFactory;

      loadedPackages = new HashMap<DeployedPackage, PackageContext>();
   }

   @Override
   public synchronized void load(DeployedPackage deployedPackage) throws PackageLoadingException {
      final List<BindingEnvironment> bindingEnvironments = bindingEnvFactory.newEnviornment(deployedPackage.resourceManager());
      final PackageContext newPackageContext = new PackageContextImpl(deployedPackage.archiveUri().toString(), new PackageBindingsImpl(bindingEnvironments));

      // Read through all of the resources
      for (Resource resource : deployedPackage.resourceManager().resources()) {
         final BindingEnvironment env = getBindingEnvironment(bindingEnvironments, resource);

         if (env != null) {
            try {
               env.load(resource);
            } catch (PackageLoadingException ble) {
               LOG.error("Failed to load deployed resource: " + deployedPackage.archiveUri() + ". Reason: " + ble.getMessage(), ble);
            }
         }
      }

      // Register the new package
      loadedPackages.put(deployedPackage, newPackageContext);
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
