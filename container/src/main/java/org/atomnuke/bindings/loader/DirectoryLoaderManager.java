package org.atomnuke.bindings.loader;

import java.io.File;
import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.atomnuke.bindings.BindingEnvironmentFactory;
import org.atomnuke.bindings.BindingLoaderException;
import org.atomnuke.container.packaging.DeployedPackage;
import org.atomnuke.container.packaging.PackageContext;
import org.atomnuke.container.packaging.PackageLoader;
import org.atomnuke.container.packaging.PackageLoaderImpl;
import org.atomnuke.container.packaging.Unpacker;
import org.atomnuke.container.packaging.UnpackerException;
import org.atomnuke.container.packaging.archive.zip.ArchiveExtractor;
import org.atomnuke.plugin.ReferenceInstantiationException;
import org.atomnuke.service.Service;
import org.atomnuke.service.ServiceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class DirectoryLoaderManager {

   private static final Logger LOG = LoggerFactory.getLogger(DirectoryLoaderManager.class);

   private final Map<DeployedPackage, PackageContext> loadedPackages;
   private final ServiceManager serviceManager;
   private final Unpacker archiveUnpacker;
   private final File libraryDirectory;

   public DirectoryLoaderManager(ServiceManager serviceManager, File deploymentDirectory, File libraryDirectory) {
      this.serviceManager = serviceManager;
      this.libraryDirectory = libraryDirectory;

      loadedPackages = new HashMap<DeployedPackage, PackageContext>();
      archiveUnpacker = new ArchiveExtractor(deploymentDirectory);
   }

   public Collection<PackageContext> loadedPackageContexts() {
      return loadedPackages.values();
   }

   public void gatherServices(PackageContext pkgContext) {
      LOG.debug("Loading services for package: " + pkgContext.name());

      try {
         for (Service svc : pkgContext.packageBindings().resolveServices()) {
            serviceManager.register(svc);
         }
      } catch (ReferenceInstantiationException bie) {
         LOG.error("Failed to init services.");
      }
   }

   public void load(BindingEnvironmentFactory bindingContextManagerFactory) throws BindingLoaderException {
      if (!libraryDirectory.exists()) {
         if (!libraryDirectory.mkdirs()) {
            throw new BindingLoaderException("Unable to make library directory: " + libraryDirectory.getAbsolutePath());
         }
      }

      if (!libraryDirectory.isDirectory()) {
         throw new BindingLoaderException(libraryDirectory.getAbsolutePath() + " is not a valid library directory.");
      }

      for (File archive : libraryDirectory.listFiles()) {
         if (archive.isDirectory()) {
            // Ignore this for now
            continue;
         }

         final URI archiveUri = archive.toURI();

         if (archiveUnpacker.canUnpack(archiveUri)) {
            final PackageLoader packageLoader = new PackageLoaderImpl(bindingContextManagerFactory);

            try {
               final DeployedPackage deployedPackage = archiveUnpacker.unpack(archiveUri);
               final PackageContext packageContext = packageLoader.load(deployedPackage);

               gatherServices(packageContext);
               loadedPackages.put(deployedPackage, packageContext);

               LOG.info("Loaded URI: " + archive.getAbsolutePath());
            } catch (UnpackerException ue) {
               throw new BindingLoaderException(ue);
            }
         }
      }
   }
}
