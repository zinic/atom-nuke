package org.atomnuke.container.packaging.resource;

import java.net.URI;
import org.atomnuke.container.packaging.archive.ResourceType;

public class ResourceDescriptorImpl implements Resource {

   private final ResourceType resourceType;
   private final URI resourceLocation;
   private final String relativePath;
   private final byte[] digestBytes;

   public ResourceDescriptorImpl(URI resourceLocation, String relativePath, ResourceType resourceType, byte[] digestBytes) {
      this.resourceLocation = resourceLocation;
      this.relativePath = relativePath;
      this.resourceType = resourceType;
      this.digestBytes = digestBytes;
   }

   @Override
   public ResourceType type() {
      return resourceType;
   }

   @Override
   public byte[] digestBytes() {
      return digestBytes;
   }

   @Override
   public String relativePath() {
      return relativePath;
   }

   @Override
   public URI location() {
      return resourceLocation;
   }
}
