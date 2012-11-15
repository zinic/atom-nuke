package org.atomnuke.container.packaging.resource;

import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ResourceManagerImpl implements ResourceManager {

   private final Map<URI, Resource> resourceMappings;
   private final Map<String, URI> resourceAliases;
   private final ResourceManager parent;

   public ResourceManagerImpl() {
      this(null, new TreeMap<URI, Resource>(), new TreeMap<String, URI>());
   }

   public ResourceManagerImpl(ResourceManager parent) {
      this(parent, new TreeMap<URI, Resource>(), new TreeMap<String, URI>());
   }

   public ResourceManagerImpl(ResourceManager parent, Map<URI, Resource> resourceMappings, Map<String, URI> resourceAliases) {
      this.resourceMappings = resourceMappings;
      this.resourceAliases = resourceAliases;
      this.parent = parent;
   }

   @Override
   public synchronized ResourceManager copy() {
      return new ResourceManagerImpl(parent, new TreeMap<URI, Resource>(resourceMappings), new TreeMap<String, URI>(resourceAliases));
   }

   @Override
   public synchronized Collection<Resource> resources() {
      final List<Resource> resourceCollection = new LinkedList<Resource>();
      
      if (parent != null) {
         resourceCollection.addAll(parent.resources());
      }
      
      resourceCollection.addAll(resourceMappings.values());
      
      return resourceCollection;
   }

   @Override
   public boolean exists(String resourcePath) {
      return resourceAliases.containsKey(resourcePath);
   }

   @Override
   public boolean exists(URI location) {
      return resourceMappings.containsKey(location);
   }

   @Override
   public synchronized Resource lookup(String path) {
      final URI aliasedResource = resourceAliases.get(path);

      if (aliasedResource == null) {
         return parent != null ? parent.lookup(path) : null;
      }
      
      return lookup(aliasedResource);
   }

   @Override
   public synchronized Resource lookup(URI path) {
      final Resource localResource = resourceMappings.get(path);

      if (localResource == null) {
         return parent != null ? parent.lookup(path) : null;
      }
      
      return localResource;
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
