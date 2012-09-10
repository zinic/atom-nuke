package org.atomnuke.atom.io.writer.stax;

import org.atomnuke.atom.io.cfg.NamespaceLevel;
import org.atomnuke.atom.io.cfg.WriterConfiguration;

/**
 *
 * @author zinic
 */
public class StaxWriterConfiguration implements WriterConfiguration {

   private final NamespaceLevel namespaceLevel;

   public StaxWriterConfiguration(NamespaceLevel namespaceLevel) {
      this.namespaceLevel = namespaceLevel;
   }

   @Override
   public NamespaceLevel getNamespaceLevel() {
      return namespaceLevel;
   }
}
