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

   @Override
   public int hashCode() {
      int hash = 73 + (this.archiveUri != null ? this.archiveUri.hashCode() : 0);

      return hash;
   }

   @Override
   public boolean equals(Object obj) {
      if (obj == null || getClass() != obj.getClass()) {
         return false;
      }

      final DeployedPackageImpl other = (DeployedPackageImpl) obj;

      if (this.archiveUri != other.archiveUri && (this.archiveUri == null || !this.archiveUri.equals(other.archiveUri))) {
         return false;
      }

      return true;
   }
}
