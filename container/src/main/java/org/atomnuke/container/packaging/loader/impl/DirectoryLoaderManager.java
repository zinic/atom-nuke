package org.atomnuke.container.packaging.loader.impl;

import org.atomnuke.container.packaging.loader.PackageLoaderManager;
import java.io.File;
import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.atomnuke.container.packaging.bindings.BindingEnvironmentFactory;
import org.atomnuke.container.packaging.bindings.PackageLoaderException;
import org.atomnuke.container.packaging.DeployedPackage;
import org.atomnuke.container.packaging.PackageContext;
import org.atomnuke.container.packaging.PackageLoader;
import org.atomnuke.container.packaging.PackageLoaderImpl;
import org.atomnuke.container.packaging.Unpacker;
import org.atomnuke.container.packaging.UnpackerException;
import org.atomnuke.container.packaging.archive.zip.ArchiveExtractor;
import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.plugin.ReferenceInstantiationException;
import org.atomnuke.service.Service;
import org.atomnuke.service.ServiceManager;
import org.atomnuke.service.context.ServiceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class DirectoryLoaderManager implements PackageLoaderManager {

   private static final Logger LOG = LoggerFactory.getLogger(DirectoryLoaderManager.class);

   private final Map<DeployedPackage, PackageContext> loadedPackages;
   private final BindingEnvironmentFactory bindingEnvFactory;
   private final ServiceManager serviceManager;
   private final Unpacker archiveUnpacker;
   private final File libraryDirectory;

   public DirectoryLoaderManager(BindingEnvironmentFactory bindingEnvFactory, ServiceManager serviceManager, File deploymentDirectory, File libraryDirectory) {
      this.bindingEnvFactory = bindingEnvFactory;
      this.serviceManager = serviceManager;
      this.libraryDirectory = libraryDirectory;

      loadedPackages = new HashMap<DeployedPackage, PackageContext>();
      archiveUnpacker = new ArchiveExtractor(deploymentDirectory);
   }

   private void gatherServices(PackageContext pkgContext) {
      LOG.debug("Loading services for package: " + pkgContext.name());

      try {
         for (InstanceContext<Service> svc : pkgContext.packageBindings().resolveServices()) {
            serviceManager.register(svc);
         }
      } catch (ReferenceInstantiationException bie) {
         LOG.error("Failed to init services.");
      }
   }

   @Override
   public String name() {
      return "Nuke stock directory package loader manager";
   }

   @Override
   public boolean provides(Class serviceInterface) {
      return serviceInterface.isAssignableFrom(PackageLoaderManager.class);
   }

   @Override
   public Object instance() {
      return this;
   }

   @Override
   public void init(ServiceContext sc) {
      try {
         load(sc);
      } catch (PackageLoaderException ple) {
         LOG.error(ple.getMessage(), ple);
      }
   }

   public void load(ServiceContext sc) throws PackageLoaderException {
      if (!libraryDirectory.exists()) {
         if (!libraryDirectory.mkdirs()) {
            throw new PackageLoaderException("Unable to make library directory: " + libraryDirectory.getAbsolutePath());
         }
      }

      if (!libraryDirectory.isDirectory()) {
         throw new PackageLoaderException(libraryDirectory.getAbsolutePath() + " is not a valid library directory.");
      }

      for (File archive : libraryDirectory.listFiles()) {
         if (archive.isDirectory()) {
            // Ignore this for now
            continue;
         }

         final URI archiveUri = archive.toURI();

         if (archiveUnpacker.canUnpack(archiveUri)) {
            final PackageLoader packageLoader = new PackageLoaderImpl(bindingEnvFactory);

            try {
               final DeployedPackage deployedPackage = archiveUnpacker.unpack(archiveUri);
               final PackageContext packageContext = packageLoader.load(deployedPackage);

               gatherServices(packageContext);
               loadedPackages.put(deployedPackage, packageContext);

               LOG.info("Loaded URI: " + archive.getAbsolutePath());
            } catch (UnpackerException ue) {
               throw new PackageLoaderException(ue);
            }
         }
      }
   }

   @Override
   public void destroy() {
   }

   @Override
   public Collection<PackageContext> loadedPackageContexts() {
      return loadedPackages.values();
   }
}
