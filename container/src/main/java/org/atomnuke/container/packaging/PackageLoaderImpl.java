package org.atomnuke.container.packaging;

import java.io.File;
import java.util.List;
import org.atomnuke.bindings.BindingEnvironmentManager;
import org.atomnuke.bindings.BindingLoaderException;
import org.atomnuke.bindings.PackageBindingsImpl;
import org.atomnuke.bindings.context.BindingEnvironment;
import org.atomnuke.container.packaging.resource.ResourceDescriptor;

/**
 *
 * @author zinic
 */
public class PackageLoaderImpl implements PackageLoader {

   private final BindingEnvironmentManager bindingEnvironmentManager;

   public PackageLoaderImpl(BindingEnvironmentManager bindingContextManagerFactory) {
      this.bindingEnvironmentManager = bindingContextManagerFactory;
   }

   @Override
   public PackageContext load(DeployedPackage deployedPackage) {
      final List<BindingEnvironment> bindingEnvironments = bindingEnvironmentManager.newEnviornmentList();
      final PackageContext newPackageContext = new PackageContextImpl(deployedPackage.archiveUri().toString(), new PackageBindingsImpl(bindingEnvironments));

      for (ResourceDescriptor resourceDescriptor : deployedPackage.resourceRegistry().resources()) {
         for (BindingEnvironment ctx : bindingEnvironments) {
            boolean foundCtx = false;

            for (String extension : ctx.language().fileExtensions()) {
               if (resourceDescriptor.deployedPath().endsWith(extension)) {
                  try {
                     ctx.load(resourceDescriptor.relativePath(), new File(resourceDescriptor.deployedPath()).toURI());
                  } catch (BindingLoaderException ble) {
                  }

                  foundCtx = true;
                  break;
               }
            }

            if (foundCtx) {
               break;
            }
         }
      }

      return newPackageContext;
   }
}
