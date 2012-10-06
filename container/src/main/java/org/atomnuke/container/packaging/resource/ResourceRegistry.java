package org.atomnuke.container.packaging.resource;

import java.util.Collection;

public interface ResourceRegistry {

   ResourceDescriptor lookupClasspath(String classPath);

   ResourceDescriptor lookupResource(String resourcePath);

   boolean hasConflictingIdentity(ResourceDescriptor resource);

   void register(ResourceDescriptor resource);

   void classpathAlias(String classpathAlias, String resourcePath);

   Collection<ResourceDescriptor> resources();
}
