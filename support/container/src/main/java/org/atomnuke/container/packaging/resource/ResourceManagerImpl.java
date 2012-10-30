package org.atomnuke.container.packaging.resource;

import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
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
   public ResourceManager copy() {
      return new ResourceManagerImpl(new TreeMap<URI, Resource>(resourceMappings),
              new TreeMap<String, URI>(resourceAliases));
   }

   @Override
   public Collection<Resource> resources() {
      return resourceMappings.values();
   }

   @Override
   public Resource lookup(String path) {
      final URI aliasedResource = resourceAliases.get(path);
      return aliasedResource != null ? lookup(aliasedResource) : null;
   }

   @Override
   public Resource lookup(URI path) {
      return resourceMappings.get(path);
   }

   @Override
   public void register(Resource resource) {
      resourceMappings.put(resource.uri(), resource);
   }

   @Override
   public void alias(String alias, URI location) {
      resourceAliases.put(alias, location);
   }

   @Override
   public boolean hasConflictingIdentity(Resource resourceDescriptor) {
      final Resource internalDescriptor = resourceMappings.get(resourceDescriptor.uri());

      if (internalDescriptor != null) {
         // If the identies are not equal then this may be a potential conflict
         return !Arrays.equals(internalDescriptor.digestBytes(), resourceDescriptor.digestBytes());
      }

      return false;
   }
}
