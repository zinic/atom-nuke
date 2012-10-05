package org.atomnuke.bindings.ear;

import com.oracle.javaee6.ApplicationType;
import com.rackspace.papi.commons.util.StringUtilities;
import org.atomnuke.container.classloader.ResourceDescriptor;
import com.rackspace.papi.commons.util.digest.MessageDigester;
import com.rackspace.papi.commons.util.digest.impl.SHA1MessageDigester;
import java.io.ByteArrayInputStream;
import org.atomnuke.container.classloader.ear.EarArchiveEntryHelper;
import org.atomnuke.container.classloader.ear.EarClassLoaderContext;
import org.atomnuke.container.classloader.ear.EarProcessingException;
import org.atomnuke.container.classloader.ear.SimpleEarClassLoaderContext;
import org.atomnuke.container.classloader.archive.ArchiveResource;
import org.atomnuke.container.classloader.archive.DeploymentAction;
import org.atomnuke.container.classloader.archive.EntryAction;
import org.atomnuke.container.classloader.archive.ProcessingAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.jar.Manifest;
import org.atomnuke.config.jee6.ApplicationXmlMarshaller;
import org.atomnuke.container.classloader.archive.ResourceType;

public class NukeEarArchiveEntryHelper implements EarArchiveEntryHelper {

   private static final Logger LOG = LoggerFactory.getLogger(NukeEarArchiveEntryHelper.class);

   private static final List<String> DEFAULT_ACCEPTED_RESOURCE_EXTENSIONS = Arrays.asList("xml", "properties");
   private static final ApplicationXmlMarshaller XML_MARSHALLER;

   static {
      try {
         XML_MARSHALLER = new ApplicationXmlMarshaller();
      } catch (JAXBException jaxbe) {
         LOG.error("Fatal error while trying to stand up the Ear Classloader JAXB parsers", jaxbe);
         throw new RuntimeException(jaxbe);
      }
   }

   private final SimpleEarClassLoaderContext context;
   private final MessageDigester messageDigester;

   public NukeEarArchiveEntryHelper(ClassLoader absoluteParent, File deploymentRoot) {
      this.context = new SimpleEarClassLoaderContext(absoluteParent, deploymentRoot);
      messageDigester = new SHA1MessageDigester();
   }

   @Override
   public EarClassLoaderContext getClassLoaderContext() {
      return context;
   }

   List<String> acceptedResourceExtensions() {
      return DEFAULT_ACCEPTED_RESOURCE_EXTENSIONS;
   }

   @Override
   public final EntryAction nextJarEntry(ArchiveResource descriptor) {
      DeploymentAction deploymentAction = descriptor.resourceType() == ResourceType.EAR
              ? DeploymentAction.UNPACK_ENTRY
              : DeploymentAction.DO_NOT_UNPACK_ENTRY;

      EntryAction entryAction = EntryAction.SKIP;

      final String extension = descriptor.extension();

      if (!StringUtilities.isBlank(extension)) {
         if (extension.equals("class")) {
            entryAction = new EntryAction(ProcessingAction.PROCESS_AS_CLASS, deploymentAction);
         } else if (extension.equals("jar")) {
            entryAction = new EntryAction(ProcessingAction.DESCEND_INTO_JAR_FORMAT_ARCHIVE, deploymentAction);
         } else {
            entryAction = new EntryAction(ProcessingAction.PROCESS_AS_RESOURCE, deploymentAction);
         }
      }

      return entryAction;
   }

   private void markLibrary() {
   }

   @Override
   public void newJarManifest(ArchiveResource name, Manifest manifest) {
   }

   @Override
   public void newClass(ArchiveResource name, byte[] classBytes) {
      final ResourceDescriptor resourceDescriptor = getResourceDescriptor(name, classBytes);

      if (!context.getClassLoader().register(resourceDescriptor)) {
         //TODO: Process conflict and identify all resources that were included with the lib archive
         markLibrary();
      }
   }

   @Override
   public void newResource(ArchiveResource name, byte[] resourceBytes) {
      final String fullArchiveName = name.fullName();

      if (fullArchiveName.equals("META-INF/application.xml") || fullArchiveName.equals("APP-INF/application.xml")) {
         readApplicationXml(fullArchiveName, resourceBytes);
      }

      LOG.debug("Resource registered " + name.fullName());

      if (!context.getClassLoader().register(getResourceDescriptor(name, resourceBytes))) {
         //TODO: Process conflict and identify all resources that were included with the lib archive
         markLibrary();
      }
   }

   private ResourceDescriptor getResourceDescriptor(ArchiveResource name, byte[] resourceBytes) {
      return new ResourceDescriptor(name, messageDigester.digestBytes(resourceBytes));
   }

   private void readApplicationXml(final String archivePath, byte[] resourceBytes) throws EarProcessingException {
      try {
         final ApplicationType type = XML_MARSHALLER.unmarhsall(new ByteArrayInputStream(resourceBytes));

         if (type != null && type.getApplicationName() != null && !StringUtilities.isBlank(type.getApplicationName().getValue())) {
            context.getEarDescriptor().setApplicationName(type.getApplicationName().getValue());
         }
      } catch (Exception ex) {
         LOG.error(EarProcessingException.ERROR_MESSAGE + ": "
                 + archivePath + "  -  Reason: " + ex.getMessage(), ex);
         throw new EarProcessingException(ex);
      }
   }
}
