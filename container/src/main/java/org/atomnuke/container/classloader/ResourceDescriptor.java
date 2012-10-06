package org.atomnuke.container.classloader;

import java.util.Arrays;
import org.atomnuke.container.packaging.archive.ResourceType;

public class ResourceDescriptor {

   private final String resourcePath;
   private final ResourceType resourceType;
   private final byte[] digestBytes;

   public ResourceDescriptor(String resourcePath, ResourceType resourceType, byte[] digestBytes) {
      this.resourcePath = resourcePath;
      this.resourceType = resourceType;
      this.digestBytes = digestBytes;
   }

   public ResourceType type() {
      return resourceType;
   }

   public byte[] digestBytes() {
      return digestBytes;
   }

   public String resourcePath() {
      return resourcePath;
   }
}
