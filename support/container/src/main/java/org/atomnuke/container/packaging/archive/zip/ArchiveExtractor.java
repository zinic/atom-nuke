package org.atomnuke.container.packaging.archive.zip;

import com.rackspace.papi.commons.util.SystemUtils;
import com.rackspace.papi.commons.util.io.MessageDigesterOutputStream;
import com.rackspace.papi.commons.util.io.OutputStreamSplitter;
import com.rackspace.papi.commons.util.io.RawInputStreamReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.atomnuke.container.packaging.resource.ResourceDescriptorImpl;
import org.atomnuke.container.packaging.resource.ResourceManagerImpl;
import org.atomnuke.container.packaging.DeployedPackage;
import org.atomnuke.container.packaging.DeployedPackageImpl;
import org.atomnuke.container.packaging.Unpacker;
import org.atomnuke.container.packaging.UnpackerException;
import org.atomnuke.container.packaging.archive.ArchiveResource;
import org.atomnuke.container.packaging.archive.ArchiveResourceImpl;
import org.atomnuke.container.packaging.archive.ResourceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class ArchiveExtractor implements Unpacker {

   private static final Logger LOG = LoggerFactory.getLogger(ArchiveExtractor.class);
   private static final MessageDigest SHA1_DIGESTER;

   static {
      try {
         SHA1_DIGESTER = MessageDigest.getInstance("SHA-1");
      } catch (NoSuchAlgorithmException nsae) {
         throw new RuntimeException("Required crypto algorithm missing. Exception: " + nsae.getMessage(), nsae);
      }
   }

   private final Queue<EmbeddedArchive> archivesToUnpack;
   private final File deploymentRoot;

   public ArchiveExtractor(File deploymentRoot) {
      this.deploymentRoot = new File(deploymentRoot, UUID.randomUUID() + "." + SystemUtils.getPid());
      archivesToUnpack = new LinkedList<EmbeddedArchive>();
   }

   @Override
   public DeployedPackage unpack(URI archiveLocation) throws UnpackerException {
      final ResourceManagerImpl resourceIdentityTree = new ResourceManagerImpl();
      final ResourceType uriType = ResourceType.findResourceTypeForName(archiveLocation.toString());

      try {
         unpackRootArchive(archiveLocation, uriType, resourceIdentityTree);
         unpackEmbeddedArchives(resourceIdentityTree, archiveLocation);

         return new DeployedPackageImpl(resourceIdentityTree, archiveLocation);
      } catch (IOException ioe) {
         throw new UnpackerException(ioe);
      }
   }

   @Override
   public boolean canUnpack(URI archiveLocation) {
      final ResourceType uriType = ResourceType.findResourceTypeForName(archiveLocation.toString());

      switch(uriType) {
         case EAR:
         case JAR:
         case ZIP:
            return true;
      }

      return false;
   }

   private void unpackZipFormatArchive(ResourceManagerImpl resourceIdentityTree, URI location, int previousDepth) throws IOException {
      final ZipInputStream rootInputStream = new ZipInputStream(location.toURL().openStream());
      ZipEntry entry;

      while ((entry = rootInputStream.getNextEntry()) != null) {
         final File extractionTarget = new File(deploymentRoot + File.separator + entry.getName());
         final URI extractionTargetUri = extractionTarget.toURI();

         if (entry.isDirectory()) {
            if (!extractionTarget.exists() && !extractionTarget.mkdirs()) {
               throw new RuntimeException("Can't make deployment directories.");
            }
         } else {
            final File parentDir = extractionTarget.getParentFile();

            if (!parentDir.exists() && !parentDir.mkdirs()) {
               throw new RuntimeException("Can't make deployment directories.");
            }

            final ArchiveResource archiveResource = new ArchiveResourceImpl(entry.getName());

            switch (archiveResource.type()) {
               case EAR:
               case JAR:
               case ZIP:
                  archivesToUnpack.add(new EmbeddedArchive(archiveResource, extractionTargetUri, previousDepth + 1));
                  break;
            }

            // Set up our output streams
            final MessageDigesterOutputStream digesterStream = new MessageDigesterOutputStream(SHA1_DIGESTER);
            final FileOutputStream fout = new FileOutputStream(extractionTarget);

            // Outputstream splitter
            final OutputStreamSplitter outputStreamSplitter = new OutputStreamSplitter(digesterStream, fout);

            // Copy to the splitter
            RawInputStreamReader.instance().copyTo(rootInputStream, outputStreamSplitter);

            // Close all streams tied to the splitter
            outputStreamSplitter.close();

            // Register the new resource
            resourceIdentityTree.register(new ResourceDescriptorImpl(extractionTargetUri, archiveResource.name(), archiveResource.type(), digesterStream.getDigest()));
            resourceIdentityTree.alias(archiveResource.name(), extractionTargetUri);
         }
      }
//      if (!new File(location).delete()) {
//         LOG.info("Failed to delete temp archive. This may make the deployment directory messy but shouldn't hurt anything.");
//      }
   }

   private void unpackRootArchive(URI archiveLocation, final ResourceType uriType, final ResourceManagerImpl resourceIdentityTree) throws IOException {
      switch (uriType) {
         case EAR:
         case JAR:
         case ZIP:
            unpackZipFormatArchive(resourceIdentityTree, archiveLocation, 0);
            break;

         default:
            LOG.error("Unknown archive format: " + archiveLocation.toString());
      }
   }

   private void unpackEmbeddedArchives(final ResourceManagerImpl resourceIdentityTree, URI archiveLocation) throws IOException {
      while (!archivesToUnpack.isEmpty()) {
         final EmbeddedArchive nextArchive = archivesToUnpack.poll();

         if (nextArchive.archiveDepth() > 5) {
            LOG.error("Cowardly refusing to recurse into archive: " + nextArchive.archiveLocation() + ". Reason: archive recursive depth limited to 5.");
         } else {
            switch (nextArchive.archiveEntry().type()) {
               case EAR:
               case JAR:
               case ZIP:
                  unpackZipFormatArchive(resourceIdentityTree, nextArchive.archiveLocation(), nextArchive.archiveDepth());
                  break;

               default:
                  LOG.error("Unknown archive format: " + archiveLocation.toString());
            }
         }
      }
   }
}
