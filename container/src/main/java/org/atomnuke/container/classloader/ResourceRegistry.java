package org.atomnuke.container.classloader;

import java.util.Set;

public interface ResourceRegistry extends Cloneable {

   /**
    *
    * @param classPath Class path definition should be delimited by '/' instead of '.'
    *
    * @return ResourceDescriptor
    */
   ResourceDescriptor resourceDescriptorFor(String classPath);

   boolean hasConflictingIdentity(ResourceDescriptor resource);

   void register(ResourceDescriptor resource);

   Set<String> registeredResources();
}
