package org.atomnuke.container.classloader;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class ResourceIdentityTree implements ResourceRegistry, Cloneable {

   private final Map<String, ResourceDescriptor> classPathIdentityTree;

   public ResourceIdentityTree() {
      classPathIdentityTree = new TreeMap<String, ResourceDescriptor>();
   }

   public ResourceIdentityTree(ResourceIdentityTree classPathIdentityTreeToCopy) {
      classPathIdentityTree = new TreeMap<String, ResourceDescriptor>(classPathIdentityTreeToCopy.classPathIdentityTree);
   }

   @Override
   public Set<String> registeredResources() {
      return new HashSet<String>(classPathIdentityTree.keySet());
   }

   @Override
   public void register(ResourceDescriptor resource) {
      classPathIdentityTree.put(resource.archiveEntry().fullName(), resource);
   }

   @Override
   public ResourceDescriptor resourceDescriptorFor(String classPath) {
      return classPathIdentityTree.get(classPath);
   }

   @Override
   public boolean hasConflictingIdentity(ResourceDescriptor resourceDescriptor) {
      final ResourceDescriptor internalDescriptor = classPathIdentityTree.get(resourceDescriptor.archiveEntry().fullName());

      if (internalDescriptor != null) {
         // If the identies are not equal then this may be a potential conflict
         return !Arrays.equals(internalDescriptor.digestBytes(), resourceDescriptor.digestBytes());
      }

      return false;
   }

   @SuppressWarnings({"CloneDoesntCallSuperClone"})
   @Override
   protected Object clone() throws CloneNotSupportedException {
      return new ResourceIdentityTree(this);
   }
}
