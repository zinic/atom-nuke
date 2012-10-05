package org.atomnuke.container.classloader.archive;

import java.util.jar.JarInputStream;

public class ArchiveStackElement {

   private final JarInputStream inputStreamReference;
   private final ArchiveResource resource;

   public ArchiveStackElement(JarInputStream inputStreamReference, ArchiveResource resource) {
      this.inputStreamReference = inputStreamReference;
      this.resource = resource;
   }

   public ArchiveResource resource() {
      return resource;
   }

   @Deprecated
   public String archiveName() {
      return resource.fullName();
   }

   public JarInputStream inputStream() {
      return inputStreamReference;
   }
}
