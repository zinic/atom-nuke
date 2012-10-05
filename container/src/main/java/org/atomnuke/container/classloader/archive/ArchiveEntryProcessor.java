package org.atomnuke.container.classloader.archive;

import com.rackspace.papi.commons.util.StringUtilities;
import com.rackspace.papi.commons.util.io.OutputStreamSplitter;
import com.rackspace.papi.commons.util.io.RawInputStreamReader;
import org.atomnuke.container.classloader.archive.util.DirectoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URI;
import java.util.jar.JarInputStream;

public class ArchiveEntryProcessor {

   private static final Logger LOG = LoggerFactory.getLogger(ArchiveEntryProcessor.class);
   private final ArchiveResource archiveResource;
   private final ArchiveEntryHelper helper;
   private final File unpackDirectory;

   public ArchiveEntryProcessor(ArchiveResource archiveEntryDescriptor, File unpackDirectory, ArchiveEntryHelper helper) {
      this.unpackDirectory = unpackDirectory;
      this.archiveResource = archiveEntryDescriptor;
      this.helper = helper;
   }

   public ArchiveStackElement processEntry(EntryAction actionToTake, ArchiveStackElement entry) throws IOException {
      ArchiveStackElement currentStackElement = entry;
      final byte[] entryBytes = loadNextEntry(currentStackElement.inputStream(), actionToTake.deploymentAction());

      switch (actionToTake.processingAction()) {
         case PROCESS_AS_CLASS:
            helper.newClass(archiveResource, entryBytes);
            break;
         case PROCESS_AS_RESOURCE:
            helper.newResource(archiveResource, entryBytes);
            break;
         case DESCEND_INTO_JAR_FORMAT_ARCHIVE:
            currentStackElement = descendIntoEntry(entryBytes);
            break;
      }

      return currentStackElement;
   }

   private byte[] loadNextEntry(JarInputStream jarInputStream, DeploymentAction packingAction) throws IOException {
      final ByteArrayOutputStream byteArrayOutput = new ByteArrayOutputStream();
      OutputStream outputStreamReference = byteArrayOutput;

      if (packingAction == DeploymentAction.UNPACK_ENTRY) {
         if (archiveResource.archiveLocation().getScheme().startsWith("file")) {
            checkParentDirectory(unpackDirectory, archiveResource);
         }

         outputStreamReference = new OutputStreamSplitter(byteArrayOutput, new FileOutputStream(unpackTargetFor(unpackDirectory, archiveResource)));
      }

      RawInputStreamReader.instance().copyTo(jarInputStream, outputStreamReference);
      outputStreamReference.close();

      // add to internal resource hashtable
      return byteArrayOutput.toByteArray();
   }

   private static File unpackTargetFor(File unpackRootDirectory, ArchiveResource resource) {
      final String prefix = resource.path();
      final File targetDir = new File(unpackRootDirectory, StringUtilities.isBlank(prefix) ? "" : prefix);

      return new File(targetDir, resource.simpleName() + "." + resource.extension());
   }

   private static void checkParentDirectory(File unpackRootDirectory, ArchiveResource resource) {
      final File targetDir = unpackTargetFor(unpackRootDirectory, resource);
      final DirectoryHelper directoryHelper = new DirectoryHelper(targetDir.getParentFile());

      if (!directoryHelper.exists() && !directoryHelper.createTargetDirectory()) {
         LOG.error("Unable to create target directory for unpacking artifact - Target directory: " + targetDir);
      }
   }

   private ArchiveStackElement descendIntoEntry(final byte[] entryBytes) throws IOException {
      final JarInputStream embeddedJarInputStream = new JarInputStream(new ByteArrayInputStream(entryBytes));

      // TODO: This totally doesn't belong here
      // ManifestProcessor.processManifest(ArchiveEntryDescriptorBuilder.build(archiveEntryDescriptor.getArchiveLocation(), ArchiveResource.ROOT_ARCHIVE, ManifestProcessor.MANIFEST_PATH), embeddedJarInputStream, helper);

      return new ArchiveStackElement(embeddedJarInputStream, archiveResource);
   }
}
