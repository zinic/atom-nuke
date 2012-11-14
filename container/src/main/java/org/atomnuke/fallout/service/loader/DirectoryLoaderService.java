package org.atomnuke.fallout.service.loader;

import org.atomnuke.container.packaging.loader.PackageLoader;
import java.io.File;
import java.net.URI;
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
import org.atomnuke.lifecycle.resolution.ResolutionActionType;
import org.atomnuke.service.Service;
import org.atomnuke.service.ServiceAlreadyRegisteredException;
import org.atomnuke.service.ServiceManager;
import org.atomnuke.service.ServiceContext;
import org.atomnuke.service.gc.ReclamationHandler;
import org.atomnuke.lifecycle.resolution.ResolutionAction;
import org.atomnuke.lifecycle.resolution.ResolutionActionImpl;
import org.atomnuke.lifecycle.InitializationException;
import org.atomnuke.service.runtime.AbstractRuntimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
@NukeBootstrap
public class DirectoryLoaderService extends AbstractRuntimeService {

   private static final String LOADER_SVC_NAME = "org.atomnuke.container.packaging.loader.impl.DirectoryLoaderService";
   private static final Logger LOG = LoggerFactory.getLogger(DirectoryLoaderService.class);

   private final BindingEnvironmentFactory bindingEnvFactory;

   private File deploymentDirectory, libraryDirectory;
   private PackageLoader packageLoader;

   public DirectoryLoaderService() {
      super(PackageLoader.class);

      bindingEnvFactory = new BindingEnvironmentManagerImpl();
   }

   @Override
   public ResolutionAction resolve(ServiceManager serviceManager) {
      return new ResolutionActionImpl(
              serviceManager.serviceRegistered(ReclamationHandler.class) ? ResolutionActionType.INIT : ResolutionActionType.DEFER);
   }

   @Override
   public String name() {
      return LOADER_SVC_NAME;
   }

   @Override
   public Object instance() {
      return packageLoader;
   }

   @Override
   public void init(ServiceContext sc) throws InitializationException {
      LOG.info("Directory package loader service starting.");

      deploymentDirectory = new File(sc.environment().deploymentDirectory());
      libraryDirectory = new File(sc.environment().libraryDirectory());
      packageLoader = new BindingAwarePackageLoader(bindingEnvFactory);

      try {
         loadPackages();
         loadServices(sc.services());
      } catch (PackageLoadingException ple) {
         LOG.error(ple.getMessage(), ple);
      }
   }

   private void loadServices(ServiceManager serviceManager) {
      for (PackageContext loadedPackage : packageLoader.packageContexts()) {
         try {
            // Gather services from the package
            gatherServices(serviceManager, loadedPackage);
         } catch (ReferenceInstantiationException bie) {
            LOG.error("Failed to init services.");
         }
      }
   }

   private void gatherServices(ServiceManager serviceManager, PackageContext pkgContext) throws ReferenceInstantiationException {
      for (InstanceContext<Service> svc : pkgContext.packageBindings().resolveServices()) {
         try {
            serviceManager.submit(svc);
         } catch (ServiceAlreadyRegisteredException sare) {
            LOG.debug("Duplicate service for name: " + svc.instance().name() + ", found in package: "
                    + pkgContext.name() + ". This version of the service will not be loaded.", sare);
         }
      }

      serviceManager.resolve();
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
            LOG.info("Extracting package \"" + archiveUri.toString() + "\"");

            try {
               final DeployedPackage deployedPackage = archiveUnpacker.unpack(archiveUri);

               LOG.info("Loading package \"" + archiveUri.toString() + "\"");
               packageLoader.load(deployedPackage);

               LOG.info("Package \"" + archive.getAbsolutePath() + "\" loaded");
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
