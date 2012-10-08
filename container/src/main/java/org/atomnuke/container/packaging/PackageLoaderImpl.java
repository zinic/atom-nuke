package org.atomnuke.container.packaging;

import java.util.List;
import org.atomnuke.bindings.BindingEnvironmentFactory;
import org.atomnuke.bindings.PackageLoaderException;
import org.atomnuke.bindings.PackageBindingsImpl;
import org.atomnuke.bindings.context.BindingEnvironment;
import org.atomnuke.container.packaging.resource.Resource;
import org.atomnuke.container.packaging.resource.ResourceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class PackageLoaderImpl implements PackageLoader {

   private static final Logger LOG = LoggerFactory.getLogger(PackageLoaderImpl.class);

   private final BindingEnvironmentFactory bindingEnvironmentManager;

   public PackageLoaderImpl(BindingEnvironmentFactory bindingContextManagerFactory) {
      this.bindingEnvironmentManager = bindingContextManagerFactory;
   }

   @Override
   public PackageContext load(DeployedPackage deployedPackage) {
      final ResourceManager resourceManager = deployedPackage.resourceManager();
      final List<BindingEnvironment> bindingEnvironments = bindingEnvironmentManager.newEnviornmentList(resourceManager);
      final PackageContext newPackageContext = new PackageContextImpl(deployedPackage.archiveUri().toString(), new PackageBindingsImpl(bindingEnvironments));

      for (Resource resource : resourceManager.resources()) {
         final BindingEnvironment env = getBindingEnvironment(bindingEnvironments, resource);

         if (env != null) {
            try {
               env.load(resource);
            } catch (PackageLoaderException ble) {
               LOG.error("Failed to load deployed resource: " + deployedPackage.archiveUri() + ". Reason: " + ble.getMessage(), ble);
            }
         }
      }

      return newPackageContext;
   }

   private BindingEnvironment getBindingEnvironment(List<BindingEnvironment> bindingEnvironments, Resource resource) {
      for (BindingEnvironment env : bindingEnvironments) {
         for (String extension : env.language().fileExtensions()) {
            if (resource.relativePath().endsWith(extension)) {
               return env;
            }
         }
      }

      return null;
   }
}
