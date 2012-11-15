package org.atomnuke.container.packaging.archive.zip;

import com.rackspace.papi.commons.util.SystemUtils;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;
import org.atomnuke.container.packaging.resource.ResourceManagerImpl;
import org.atomnuke.container.packaging.DeployedPackage;
import org.atomnuke.container.packaging.DeployedPackageImpl;
import org.atomnuke.container.packaging.Unpacker;
import org.atomnuke.container.packaging.UnpackerException;
import org.atomnuke.container.packaging.archive.ResourceType;
import org.atomnuke.container.packaging.resource.ResourceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class ArchiveExtractor implements Unpacker {

   private static final Logger LOG = LoggerFactory.getLogger(ArchiveExtractor.class);
   
   private final Queue<EmbeddedArchive> archivesToUnpack;
   private final File deploymentRoot;

   public ArchiveExtractor(File deploymentRoot) {
      this.deploymentRoot = deploymentRoot;
      
      archivesToUnpack = new LinkedList<EmbeddedArchive>();
   }

   @Override
   public DeployedPackage unpack(ResourceManager rootResourceManager, URI archiveLocation) throws UnpackerException {
      final ZipUnpacker zipUnpacker = new ZipUnpacker(rootResourceManager, new File(deploymentRoot, UUID.randomUUID() + "." + SystemUtils.getPid()));
      
      final ResourceType uriType = ResourceType.findResourceTypeForName(archiveLocation.toString());
      final ResourceManager localResourceManager = new ResourceManagerImpl(rootResourceManager);
      
      try {
         unpackArchive(localResourceManager, zipUnpacker, uriType, archiveLocation, 0);
         unpackEmbeddedArchives(localResourceManager, zipUnpacker);

         return new DeployedPackageImpl(localResourceManager, archiveLocation);
      } catch (IOException ioe) {
         throw new UnpackerException(ioe);
      }
   }

   @Override
   public boolean canUnpack(URI archiveLocation) {
      final ResourceType uriType = ResourceType.findResourceTypeForName(archiveLocation.toString());

      switch (uriType) {
         case EAR:
         case JAR:
         case ZIP:
            return true;

         default:
            return false;
      }
   }

   private void unpackArchive(ResourceManager localResourceManager, ZipUnpacker zipUnpacker, ResourceType uriType, URI archiveLocation, int archiveDepth) throws IOException {
      switch (uriType) {
         case EAR:
         case JAR:
         case ZIP:
            archivesToUnpack.addAll(zipUnpacker.unpack(localResourceManager, archiveLocation, archiveDepth));
            break;

         default:
            LOG.error("Unknown archive format: " + archiveLocation.toString());
      }
   }

   private void unpackEmbeddedArchives(ResourceManager localResourceManager, ZipUnpacker zipUnpacker) throws IOException {
      while (!archivesToUnpack.isEmpty()) {
         final EmbeddedArchive nextArchive = archivesToUnpack.poll();

         if (nextArchive.archiveDepth() > 5) {
            LOG.error("Cowardly refusing to recurse into archive: " + nextArchive.archiveLocation() + ". Reason: archive recursive depth limited to 5.");
         } else if (canUnpack(nextArchive.archiveLocation())) {
            unpackArchive(localResourceManager, zipUnpacker, nextArchive.archiveEntry().type(), nextArchive.archiveLocation(), nextArchive.archiveDepth());
         }
      }
   }
}
