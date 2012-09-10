package org.atomnuke.atom.stax;

import org.atomnuke.atom.WriterConfiguration;
import javax.xml.stream.XMLStreamWriter;

/**
 * @deprecated org.atomnuke.atom.io replaces this package
 *
 * @author zinic
 */

@Deprecated
public class WriterContext {

   private final WriterConfiguration configuration;
   private final XMLStreamWriter writer;

   public WriterContext(XMLStreamWriter writer, WriterConfiguration configuration) {
      this.configuration = configuration;
      this.writer = writer;
   }

   public WriterConfiguration getConfiguration() {
      return configuration;
   }

   public XMLStreamWriter getWriter() {
      return writer;
   }
}
