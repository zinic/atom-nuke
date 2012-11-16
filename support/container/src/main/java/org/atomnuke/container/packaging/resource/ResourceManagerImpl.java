package org.atomnuke.container.packaging.resource;

import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

public class ResourceManagerImpl implements ResourceManager {

   private final Map<URI, Resource> resourceMappings;
   private final Map<String, URI> resourceAliases;

   public ResourceManagerImpl() {
      this(new TreeMap<URI, Resource>(), new TreeMap<String, URI>());
   }
   
   public ResourceManagerImpl(Map<URI, Resource> resourceMappings, Map<String, URI> resourceAliases) {
      this.resourceMappings = resourceMappings;
      this.resourceAliases = resourceAliases;
   }

   @Override
   public synchronized ResourceManager copy() {
      return new ResourceManagerImpl(new TreeMap<URI, Resource>(resourceMappings), new TreeMap<String, URI>(resourceAliases));
   }

   @Override
   public synchronized Collection<Resource> resources() {
      return new LinkedList<Resource>(resourceMappings.values());
   }
   
   @Override
   public synchronized boolean exists(String resourcePath) {
      return resourceAliases.containsKey(resourcePath);
   }

   @Override
   public synchronized boolean exists(URI location) {
      return resourceMappings.containsKey(location);
   }

   @Override
   public synchronized Resource lookup(String path) {
      final URI aliasedResource = resourceAliases.get(path);

      return aliasedResource != null ? lookup(aliasedResource) : null;
   }

   @Override
   public synchronized Resource lookup(URI path) {
      return resourceMappings.get(path);
   }

   @Override
   public synchronized void register(Resource resource) {
      resourceMappings.put(resource.uri(), resource);
   }

   @Override
   public synchronized void alias(String alias, URI location) {
      resourceAliases.put(alias, location);
   }

   @Override
   public boolean conflicts(URI path, byte[] digest) {
      final Resource internalDescriptor = resourceMappings.get(path);

      if (internalDescriptor != null) {
         // If the identies are not equal then this may be a potential conflict
         return !Arrays.equals(internalDescriptor.digestBytes(), digest);
      }

      return false;
   }
}
