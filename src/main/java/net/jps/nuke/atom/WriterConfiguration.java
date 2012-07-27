package net.jps.nuke.atom;

/**
 *
 * @author zinic
 */
public class WriterConfiguration {

   public enum NamespaceLevel {

      PREFXIED,
      NONE
   }
   private final NamespaceLevel namespaceLevel;

   public WriterConfiguration(NamespaceLevel namespaceLevel) {
      this.namespaceLevel = namespaceLevel;
   }

   public NamespaceLevel getNamespaceLevel() {
      return namespaceLevel;
   }
}
