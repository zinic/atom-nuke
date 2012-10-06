package org.atomnuke.container.packaging;

import java.net.URI;
import org.atomnuke.container.classloader.ResourceRegistry;

/**
 *
 * @author zinic
 */
public class DeployedPackageImpl implements DeployedPackage {

   private final ResourceRegistry resourceRegistry;
   private final URI archiveUri;

   public DeployedPackageImpl(ResourceRegistry resourceRegistry, URI archiveUri) {
      this.resourceRegistry = resourceRegistry;
      this.archiveUri = archiveUri;
   }

   @Override
   public URI archiveUri() {
      return archiveUri;
   }

   @Override
   public ResourceRegistry resourceRegistry() {
      return resourceRegistry;
   }
}
