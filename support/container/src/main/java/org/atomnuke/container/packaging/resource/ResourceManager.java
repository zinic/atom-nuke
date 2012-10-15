package org.atomnuke.container.packaging.resource;

import java.net.URI;
import java.util.Collection;

public interface ResourceManager {

   ResourceManager copy();

   Collection<Resource> resources();

   Resource lookup(String resourcePath);

   Resource lookup(URI location);

   boolean hasConflictingIdentity(Resource resource);

   void register(Resource resource);

   void alias(String classpathAlias, URI location);
}
