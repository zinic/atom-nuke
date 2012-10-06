package org.atomnuke.container.classloader;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

public class ResourceIdentityTree implements ResourceRegistry {

   private final Map<String, ResourceDescriptor> resourceIdentityTree;
   private final Map<String, String> classPathIdentityMappings;

   public ResourceIdentityTree() {
      resourceIdentityTree = new TreeMap<String, ResourceDescriptor>();
      classPathIdentityMappings = new TreeMap<String, String>();
   }

   public ResourceIdentityTree(ResourceIdentityTree classPathIdentityTreeToCopy) {
      resourceIdentityTree = new TreeMap<String, ResourceDescriptor>(classPathIdentityTreeToCopy.resourceIdentityTree);
      classPathIdentityMappings = new TreeMap<String, String>(classPathIdentityTreeToCopy.classPathIdentityMappings);
   }

   @Override
   public Collection<ResourceDescriptor> resources() {
      return resourceIdentityTree.values();
   }

   @Override
   public ResourceDescriptor lookupResource(String resourcePath) {
      return resourceIdentityTree.get(resourcePath);
   }

   @Override
   public void register(ResourceDescriptor resource) {
      resourceIdentityTree.put(resource.resourcePath(), resource);
   }

   @Override
   public void classpathAlias(String classpathAlias, String resourcePath) {
      classPathIdentityMappings.put(classpathAlias, resourcePath);
   }

   @Override
   public ResourceDescriptor lookupClasspath(String classPath) {
      final String resourcePath = classPathIdentityMappings.get(classPath);

      return resourcePath != null ? resourceIdentityTree.get(resourcePath) : null;
   }

   @Override
   public boolean hasConflictingIdentity(ResourceDescriptor resourceDescriptor) {
      final ResourceDescriptor internalDescriptor = resourceIdentityTree.get(resourceDescriptor.resourcePath());

      if (internalDescriptor != null) {
         // If the identies are not equal then this may be a potential conflict
         return !Arrays.equals(internalDescriptor.digestBytes(), resourceDescriptor.digestBytes());
      }

      return false;
   }
}
