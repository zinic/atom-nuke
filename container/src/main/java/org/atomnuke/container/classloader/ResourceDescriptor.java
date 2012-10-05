package org.atomnuke.container.classloader;

import org.atomnuke.container.classloader.archive.ArchiveResource;

import java.util.Arrays;

public class ResourceDescriptor {

   private final ArchiveResource descriptor;
   private final byte[] digestBytes;

   public ResourceDescriptor(ArchiveResource descriptor, byte[] digestBytes) {
      this.descriptor = descriptor;
      this.digestBytes = Arrays.copyOf(digestBytes, digestBytes.length);
   }

   public byte[] digestBytes() {
      return digestBytes;
   }

   public ArchiveResource archiveEntry() {
      return descriptor;
   }
}
