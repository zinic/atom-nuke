package org.atomnuke.container.classloader.archive;

import java.net.URI;

public class ArchiveResource {

   private final String fullName, resourcePath, simpleName, extension;
   private final ResourceType resourceType;
   private final URI archiveLocation;

   public ArchiveResource(String resourcePath, String simpleName, String extension, URI archiveLocation) {
      resourceType = ResourceType.findResourceTypeForExtension(extension);

      this.resourcePath = resourcePath;
      this.simpleName = simpleName;
      this.extension = extension;
      this.archiveLocation = archiveLocation;
      this.fullName = (resourcePath.endsWith("/") ? resourcePath : resourcePath + "/") + simpleName + "." + extension;
   }

   public ResourceType resourceType() {
      return resourceType;
   }

   public URI archiveLocation() {
      return archiveLocation;
   }

   public String fullName() {
      return fullName;
   }

   public String path() {
      return resourcePath;
   }

   public String simpleName() {
      return simpleName;
   }

   public String extension() {
      return extension;
   }
}
