package org.atomnuke.container.packaging.archive;

/**
 *
 * @author zinic
 */
public class ArchiveResourceImpl implements ArchiveResource {

   private final ResourceType type;
   private final String name;

   public ArchiveResourceImpl(String name) {
      this.name = name;
      this.type = ResourceType.findResourceTypeForName(name);
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
