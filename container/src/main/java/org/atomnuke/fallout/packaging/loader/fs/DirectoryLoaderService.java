package org.atomnuke.fallout.packaging.loader.fs;

import org.atomnuke.container.packaging.loader.PackageLoader;
import java.io.File;
import java.net.URI;
import java.util.Collections;
import org.atomnuke.NukeEnv;
import org.atomnuke.container.packaging.bindings.BindingEnvironmentFactory;
import org.atomnuke.container.packaging.bindings.PackageLoadingException;
import org.atomnuke.container.packaging.DeployedPackage;
import org.atomnuke.container.packaging.PackageContext;
import org.atomnuke.container.packaging.Unpacker;
import org.atomnuke.container.packaging.UnpackerException;
import org.atomnuke.container.packaging.archive.zip.ArchiveExtractor;
import org.atomnuke.container.packaging.bindings.environment.BindingEnvironmentManagerImpl;
import org.atomnuke.container.packaging.loader.impl.BindingAwarePackageLoader;
import org.atomnuke.container.service.annotation.NukeBootstrap;
import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.plugin.ReferenceInstantiationException;
import org.atomnuke.service.Service;
import org.atomnuke.service.ServiceAlreadyRegisteredException;
import org.atomnuke.service.ServiceManager;
import org.atomnuke.service.context.ServiceContext;
import org.atomnuke.service.context.ServiceContextImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
@NukeBootstrap
public class DirectoryLoaderService implements Service {

   private static final String LOADER_SVC_NAME = "org.atomnuke.container.packaging.loader.impl.DirectoryLoaderService";
   private static final Logger LOG = LoggerFactory.getLogger(DirectoryLoaderService.class);

   private final BindingEnvironmentFactory bindingEnvFactory;
   private final PackageLoader packageLoader;

   private File deploymentDirectory, libraryDirectory;

   public DirectoryLoaderService() {
      this.deploymentDirectory = new File(NukeEnv.NUKE_DEPLOY);
      this.libraryDirectory = new File(NukeEnv.NUKE_LIB);

      bindingEnvFactory = new BindingEnvironmentManagerImpl();

      packageLoader = new BindingAwarePackageLoader(bindingEnvFactory);
   }

   @Override
   public String name() {
      return LOADER_SVC_NAME;
   }

   @Override
   public boolean provides(Class serviceInterface) {
      return serviceInterface.isAssignableFrom(PackageLoader.class);
   }

   @Override
   public Object instance() {
      return packageLoader;
   }

   @Override
   public void init(ServiceContext sc) {
      LOG.info("Directory package loader service starting.");

      try {
         loadPackages();
         loadServices(sc.manager());
      } catch (PackageLoadingException ple) {
         LOG.error(ple.getMessage(), ple);
      }
   }

   private void loadServices(ServiceManager serviceManager) {
      for (PackageContext loadedPackage : packageLoader.packageContexts()) {
         // Gather services from the package
         try {
            gatherServices(serviceManager, loadedPackage);
         } catch (ReferenceInstantiationException bie) {
            LOG.error("Failed to init services.");
         }
      }
   }

   private void gatherServices(ServiceManager serviceManager, PackageContext pkgContext) throws ReferenceInstantiationException {
      final ServiceContext serviceContext = new ServiceContextImpl(serviceManager, Collections.EMPTY_MAP);

      for (InstanceContext<Service> svc : pkgContext.packageBindings().resolveServices()) {
         try {
            serviceManager.register(svc);
            initService(svc, serviceContext);
         } catch (ServiceAlreadyRegisteredException sare) {
            LOG.debug("Duplicate service for name: " + svc.instance().name() + ", found in package: "
                    + pkgContext.name() + ". This version of the service will not be loaded.");
         }
      }
   }

   private void initService(InstanceContext<Service> svc, ServiceContext serviceContext) {
      try {
         svc.environment().stepInto();
         svc.instance().init(serviceContext);
      } finally {
         svc.environment().stepOut();
      }
   }

   private void loadPackages() throws PackageLoadingException {
      final Unpacker archiveUnpacker = new ArchiveExtractor(deploymentDirectory);

      if (!libraryDirectory.exists()) {
         if (!libraryDirectory.mkdirs()) {
            throw new PackageLoadingException("Unable to make library directory: " + libraryDirectory.getAbsolutePath());
         }
      }

      if (!libraryDirectory.isDirectory()) {
         throw new PackageLoadingException(libraryDirectory.getAbsolutePath() + " is not a valid library directory.");
      }

      for (File archive : libraryDirectory.listFiles()) {
         if (archive.isDirectory()) {
            // Ignore this for now
            continue;
         }

         final URI archiveUri = archive.toURI();

         if (archiveUnpacker.canUnpack(archiveUri)) {
            LOG.info("Loading URI: " + archive.getAbsolutePath());

            try {
               final DeployedPackage deployedPackage = archiveUnpacker.unpack(archiveUri);
               packageLoader.load(deployedPackage);

               LOG.info("Loaded URI: " + archive.getAbsolutePath());
            } catch (UnpackerException ue) {
               LOG.error("Failed to unpack package (" + archive.getAbsolutePath() + ") - Reason: " + ue.getMessage(), ue);
            } catch (PackageLoadingException ple) {
               LOG.error("Failed to load package (" + archive.getAbsolutePath() + ") - Reason: " + ple.getMessage(), ple);
            }
         }
      }
   }

   @Override
   public void destroy() {
      LOG.info("Directory package loader service stopping.");
   }
}
