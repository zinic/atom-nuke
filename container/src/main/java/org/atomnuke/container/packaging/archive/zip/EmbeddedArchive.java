package org.atomnuke.container.packaging.archive.zip;

import java.net.URI;
import org.atomnuke.container.packaging.archive.ArchiveResource;

/**
 *
 * @author zinic
 */
public class EmbeddedArchive {

   private final ArchiveResource archiveEntry;
   private final URI archiveLocation;
   private final int archiveDepth;

   public EmbeddedArchive(ArchiveResource archiveEntry, URI archiveLocation, int archiveDepth) {
      this.archiveEntry = archiveEntry;
      this.archiveLocation = archiveLocation;
      this.archiveDepth = archiveDepth;
   }

   public ArchiveResource archiveEntry() {
      return archiveEntry;
   }

   public URI archiveLocation() {
      return archiveLocation;
   }

   public int archiveDepth() {
      return archiveDepth;
   }
}
