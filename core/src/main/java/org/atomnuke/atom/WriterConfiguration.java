package org.atomnuke.atom;

/**
 * @deprecated org.atomnuke.atom.io replaces this package
 *
 * @author zinic
 */

@Deprecated
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
