package org.atomnuke.container.packaging.archive;

/**
 *
 * @author zinic
 */
public class ArchiveResourceImpl implements ArchiveResource {

   private final ResourceType type;
   private final String name;

   public ArchiveResourceImpl(String name) {
      this(name, ResourceType.findResourceTypeForName(name));
   }

   public ArchiveResourceImpl(String name, ResourceType resourceType) {
      this.name = name;
      this.type = resourceType;
   }

   @Override
   public String name() {
      return name;
   }

   @Override
   public ResourceType type() {
      return type;
   }
}
