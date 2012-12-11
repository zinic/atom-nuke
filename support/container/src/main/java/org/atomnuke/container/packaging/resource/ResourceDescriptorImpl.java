package org.atomnuke.container.packaging.resource;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import org.atomnuke.container.packaging.archive.ResourceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceDescriptorImpl implements Resource {

   private static final Logger LOG = LoggerFactory.getLogger(ResourceDescriptorImpl.class);

   private final ResourceType resourceType;
   private final URI resourceUri;
   private final URL resourceUrl;
   private final String relativePath;
   private final byte[] digestBytes;

   public ResourceDescriptorImpl(URI resourceUri, String relativePath, ResourceType resourceType) {
      this(resourceUri, relativePath, resourceType, new byte[0]);
   }

   public ResourceDescriptorImpl(URI resourceUri, String relativePath, ResourceType resourceType, byte[] digestBytes) {
      this.resourceUri = resourceUri;
      this.resourceUrl = toUrl(resourceUri);

      this.relativePath = relativePath;
      this.resourceType = resourceType;
      this.digestBytes = Arrays.copyOf(digestBytes, digestBytes.length);
   }

   public static URL toUrl(URI descriptor) {
      try {
         return descriptor.toURL();
      } catch (MalformedURLException murle) {
         LOG.error("Error building location for resource: " + descriptor);
         return null;
      }
   }

   @Override
   public URL url() {
      return resourceUrl;
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
   public URI uri() {
      return resourceUri;
   }
}
