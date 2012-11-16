package org.atomnuke.container.packaging.archive.zip;

import com.rackspace.papi.commons.util.io.MessageDigesterOutputStream;
import com.rackspace.papi.commons.util.io.OutputStreamSplitter;
import com.rackspace.papi.commons.util.io.RawInputStreamReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.atomnuke.container.packaging.archive.ArchiveResource;
import org.atomnuke.container.packaging.archive.ArchiveResourceImpl;
import org.atomnuke.container.packaging.archive.ResourceType;
import org.atomnuke.container.packaging.resource.Resource;
import org.atomnuke.container.packaging.resource.ResourceDescriptorImpl;
import org.atomnuke.container.packaging.resource.ResourceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class ZipUnpacker {

   private static final Logger LOG = LoggerFactory.getLogger(ZipUnpacker.class);
   private static final MessageDigest SHA1_DIGESTER;

   static {
      try {
         SHA1_DIGESTER = MessageDigest.getInstance("SHA-1");
      } catch (NoSuchAlgorithmException nsae) {
         throw new RuntimeException("Required crypto algorithm missing. Exception: " + nsae.getMessage(), nsae);
      }
   }
   private final ResourceManager rootResourceManager;
   private final File deploymentRoot;

   public ZipUnpacker(ResourceManager rootResourceManager, File deploymentRoot) {
      this.rootResourceManager = rootResourceManager;
      this.deploymentRoot = deploymentRoot;
   }

   private static String formatEntryName(String entryName) {
      return entryName.endsWith("/") ? entryName.substring(0, entryName.length() - 1) : entryName;

   }

   public List<EmbeddedArchive> unpack(ResourceManager localResourceManager, URI location, int previousDepth) throws IOException {
      final List<EmbeddedArchive> embeddedArchives = new LinkedList<EmbeddedArchive>();

      // Open up our input stream
      final ZipInputStream rootInputStream = new ZipInputStream(location.toURL().openStream());
      ZipEntry entry;

      // Read through all of the entries
      while ((entry = rootInputStream.getNextEntry()) != null) {
         final ArchiveResource resourceInfo = new ArchiveResourceImpl(formatEntryName(entry.getName()));

         // We handle directories a little different
         if (entry.isDirectory()) {
            registerArchiveDirectory(resourceInfo);
         } else {
            register(localResourceManager, rootInputStream, embeddedArchives, resourceInfo, previousDepth);
         }
      }

      return embeddedArchives;
   }

   private void registerArchiveDirectory(ArchiveResource archiveResource) throws RuntimeException {
      if (!rootResourceManager.exists(archiveResource.name())) {
         final File desiredLocation = new File(deploymentRoot + File.separator + archiveResource.name());

         if (!desiredLocation.exists() && !desiredLocation.mkdirs()) {
            throw new RuntimeException("Can't make deployment directories.");
         }

         final URI locationUri = desiredLocation.toURI();

         rootResourceManager.register(new ResourceDescriptorImpl(locationUri, archiveResource.name(), ResourceType.DIRECTORY));
         rootResourceManager.alias(archiveResource.name(), locationUri);
      }
   }

   private void register(ResourceManager localResourceManager, ZipInputStream rootInputStream, List<EmbeddedArchive> embeddedArchives, ArchiveResource archiveResource, int previousDepth) throws IOException, FileNotFoundException {
      final File fsTargetLocation = new File(deploymentRoot + File.separator + archiveResource.name());
      final File parentDir = fsTargetLocation.getParentFile();

      if (!parentDir.exists() && !parentDir.mkdirs()) {
         throw new RuntimeException("Can't make deployment directories.");
      }

      // Set up our output streams and extract to the slotted location regardless
      final byte[] digest = extractNextArchiveItemTo(rootInputStream, fsTargetLocation).getDigest();
      ResourceManager targetResrouceManager = rootResourceManager;

      // If there's an instance of this resource, we might need to slot it
      final Resource conflictingResource = rootResourceManager.lookup(archiveResource.name());

      // Should we slot this resource?
      if (conflictingResource != null) {
         targetResrouceManager = localResourceManager;

         // Does the resource conflict? If not, remove what we just extracted and don't register this resource
         if (!rootResourceManager.conflicts(conflictingResource.uri(), digest)) {
            fsTargetLocation.delete();
            return;
         }
         
         LOG.info("Slotting conflicted resource: " + fsTargetLocation.toURI());
      }

      // Regsiter our new resource
      targetResrouceManager.register(new ResourceDescriptorImpl(fsTargetLocation.toURI(), archiveResource.name(), archiveResource.type(), digest));
      targetResrouceManager.alias(archiveResource.name(), fsTargetLocation.toURI());

      // Add embedded archives for processing later
      switch (archiveResource.type()) {
         case EAR:
         case JAR:
         case ZIP:
            embeddedArchives.add(new EmbeddedArchive(archiveResource, fsTargetLocation.toURI(), previousDepth + 1));
            break;
      }
   }

   private MessageDigesterOutputStream extractNextArchiveItemTo(ZipInputStream archiveInputStream, File fsTargetLocation) throws IOException, FileNotFoundException {
      final MessageDigesterOutputStream digesterStream = new MessageDigesterOutputStream(SHA1_DIGESTER);
      final FileOutputStream fout = new FileOutputStream(fsTargetLocation);

      // Outputstream splitter
      final OutputStreamSplitter outputStreamSplitter = new OutputStreamSplitter(digesterStream, fout);

      // Copy to the splitter
      RawInputStreamReader.instance().copyTo(archiveInputStream, outputStreamSplitter);

      // Close all streams tied to the splitter
      outputStreamSplitter.close();

      return digesterStream;
   }
}
