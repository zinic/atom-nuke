package org.atomnuke.container.packaging;

import java.io.File;
import org.atomnuke.bindings.BindingContextManager;
import org.atomnuke.bindings.BindingContextManagerFactory;
import org.atomnuke.bindings.BindingLoaderException;
import org.atomnuke.bindings.context.BindingContext;
import org.atomnuke.container.classloader.ResourceDescriptor;

/**
 *
 * @author zinic
 */
public class PackageLoaderImpl implements PackageLoader {

   private final BindingContextManagerFactory bindingContextManagerFactory;

   public PackageLoaderImpl(BindingContextManagerFactory bindingContextManagerFactory) {
      this.bindingContextManagerFactory = bindingContextManagerFactory;
   }

   @Override
   public PackageContext load(DeployedPackage deployedPackage) {
      final BindingContextManager bindingContextManager = bindingContextManagerFactory.newManager();
      final PackageContext newPackageContext = new PackageContextImpl(deployedPackage.archiveUri().toString(), bindingContextManager);

      for (ResourceDescriptor resourceDescriptor : deployedPackage.resourceRegistry().resources()) {
         for (BindingContext ctx : bindingContextManager.availableContexts()) {
            boolean foundCtx = false;

            for (String extension : ctx.language().fileExtensions()) {
               if (resourceDescriptor.resourcePath().endsWith(extension)) {
                  try {
                     ctx.load(new File(resourceDescriptor.resourcePath()).toURI());
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
