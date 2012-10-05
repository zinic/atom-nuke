package org.atomnuke.container.classloader.archive;

import java.net.URI;

public final class ArchiveResourceBuilder {

   private static final ArchiveResourceBuilder INSTANCE = new ArchiveResourceBuilder();

   public static ArchiveResourceBuilder instance() {
      return INSTANCE;
   }

   private ArchiveResourceBuilder() {
   }

   public ArchiveResource build(URI archiveLocation, String archiveName, String entryName) {
      final String sanitizedEntryName = entryName; //StringUtilities.trim(entryName, "/");
      final int lastSeperatorIndex = sanitizedEntryName.lastIndexOf('/');

      final String resourceName = lastSeperatorIndex > 0 ? sanitizedEntryName.substring(lastSeperatorIndex + 1, sanitizedEntryName.length()) : sanitizedEntryName;
      final String resourcePath = lastSeperatorIndex > 0 ? sanitizedEntryName.substring(0, lastSeperatorIndex + 1) : "";

      final int extensionIndex = resourceName.lastIndexOf('.');

      ArchiveResource archiveEntryDescriptor = null;

      if (extensionIndex > 0) {
         final String simpleName = resourceName.substring(0, extensionIndex);
         final String extension = resourceName.substring(extensionIndex + 1, resourceName.length());

         archiveEntryDescriptor = new ArchiveResource(archiveName, resourcePath, simpleName, extension, archiveLocation);
      } else {
         archiveEntryDescriptor = new ArchiveResource(archiveName, resourcePath, resourceName, "", archiveLocation);
      }

      return archiveEntryDescriptor;
   }
}
