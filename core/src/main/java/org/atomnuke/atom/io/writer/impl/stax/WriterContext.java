package org.atomnuke.atom.io.writer.impl.stax;

import javax.xml.stream.XMLStreamWriter;

/**
 *
 * @author zinic
 */
public class WriterContext {

   private final StaxWriterConfiguration configuration;
   private final XMLStreamWriter writer;

   public WriterContext(XMLStreamWriter writer, StaxWriterConfiguration configuration) {
      this.configuration = configuration;
      this.writer = writer;
   }

   public StaxWriterConfiguration getConfiguration() {
      return configuration;
   }

   public XMLStreamWriter getWriter() {
      return writer;
   }
}
