package org.atomnuke.container.packaging;

import java.net.URI;
import org.atomnuke.container.packaging.resource.ResourceManager;

/**
 *
 * @author zinic
 */
public class DeployedPackageImpl implements DeployedPackage {

   private final ResourceManager resourceRegistry;
   private final URI archiveUri;

   public DeployedPackageImpl(ResourceManager resourceRegistry, URI archiveUri) {
      this.resourceRegistry = resourceRegistry;
      this.archiveUri = archiveUri;
   }

   @Override
   public URI archiveUri() {
      return archiveUri;
   }

   @Override
   public ResourceManager resourceManager() {
      return resourceRegistry;
   }
}
