package org.atomnuke.container.packaging.resource;

import org.atomnuke.container.packaging.archive.ResourceType;

public class ResourceDescriptor {

   private final String deployedPath;
   private final String relativePath;
   private final ResourceType resourceType;
   private final byte[] digestBytes;

   public ResourceDescriptor(String deployedPath, String relativePath, ResourceType resourceType, byte[] digestBytes) {
      this.deployedPath = deployedPath;
      this.relativePath = relativePath;
      this.resourceType = resourceType;
      this.digestBytes = digestBytes;
   }

   public ResourceType type() {
      return resourceType;
   }

   public byte[] digestBytes() {
      return digestBytes;
   }

   public String relativePath() {
      return relativePath;
   }

   public String deployedPath() {
      return deployedPath;
   }
}
