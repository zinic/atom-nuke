package org.atomnuke.container.packaging.resource;

import java.net.URI;
import java.util.Collection;

public interface ResourceManager {

   ResourceManager copy();

   Collection<Resource> resources();

   boolean exists(String resourcePath);

   boolean exists(URI location);

   Resource lookup(String resourcePath);

   Resource lookup(URI location);

   boolean conflicts(URI path, byte[] digest);

   void register(Resource resource);

   void alias(String classpathAlias, URI location);
}
