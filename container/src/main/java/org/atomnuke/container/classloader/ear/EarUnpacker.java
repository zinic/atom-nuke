package org.atomnuke.container.classloader.ear;

import org.atomnuke.container.classloader.archive.ArchiveStackElement;
import org.atomnuke.container.classloader.archive.EntryAction;
import org.atomnuke.container.classloader.archive.ProcessingAction;
import org.atomnuke.container.classloader.archive.ArchiveResource;
import org.atomnuke.container.classloader.archive.ArchiveEntryProcessor;
import org.atomnuke.container.classloader.archive.ArchiveResourceBuilder;
import com.rackspace.papi.commons.util.SystemUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Stack;
import java.util.UUID;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class EarUnpacker {

   private final ArchiveResourceBuilder archiveResourceBuilder;
   private final File deploymentDirectory;

   public EarUnpacker(File deploymentDirectory) {
      this(deploymentDirectory, ArchiveResourceBuilder.instance());
   }

   public EarUnpacker(File rootDeploymentDirectory, ArchiveResourceBuilder archiveResourceBuilder) {
      final StringBuilder deployDir = new StringBuilder(UUID.randomUUID().toString()).append(".").append(SystemUtils.getPid());
      deploymentDirectory = new File(rootDeploymentDirectory, deployDir.toString());

      this.archiveResourceBuilder = archiveResourceBuilder;
   }

   public File getDeploymentDirectory() {
      return deploymentDirectory;
   }

   public EarClassLoaderContext read(EarArchiveEntryHelper entryListener, File earFile) throws IOException {
      return read(entryListener, earFile.toURI());
   }

   public EarClassLoaderContext read(EarArchiveEntryHelper entryListener, URI location) throws IOException {
      final JarInputStream jarInputStream = new JarInputStream(location.toURL().openStream());
      final Stack<ArchiveStackElement> archiveStack = new Stack<ArchiveStackElement>();
      archiveStack.push(new ArchiveStackElement(jarInputStream, archiveResourceBuilder.build(location, location.toString(), location.getPath())));

      while (archiveStack.size() > 0) {
         ArchiveStackElement currentStackElement = archiveStack.pop();
         JarEntry nextJarEntry = currentStackElement.inputStream().getNextJarEntry();

         while (nextJarEntry != null) {
            final ArchiveResource entryDescriptor = archiveResourceBuilder.build(currentStackElement.resource().archiveLocation(), currentStackElement.resource().fullName(), nextJarEntry.getName());
            final EntryAction actionToTake = entryDescriptor != null ? entryListener.nextJarEntry(entryDescriptor) : EntryAction.SKIP;

            if (actionToTake.processingAction() != ProcessingAction.SKIP) {
               final ArchiveEntryProcessor archiveEntryProcessor = new ArchiveEntryProcessor(entryDescriptor, deploymentDirectory, entryListener);
               final ArchiveStackElement newStackElement = archiveEntryProcessor.processEntry(actionToTake, currentStackElement);

               if (!newStackElement.equals(currentStackElement)) {
                  archiveStack.push(currentStackElement);
                  currentStackElement = newStackElement;
               }
            }

            nextJarEntry = currentStackElement.inputStream().getNextJarEntry();
         }

         currentStackElement.inputStream().close();
      }

      jarInputStream.close();

      return entryListener.getClassLoaderContext();
   }
}
