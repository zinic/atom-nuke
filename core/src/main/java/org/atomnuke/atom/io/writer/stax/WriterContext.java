package org.atomnuke.atom.io.writer.stax;

import javax.xml.stream.XMLStreamWriter;
import org.atomnuke.atom.io.cfg.WriterConfiguration;

/**
 *
 * @author zinic
 */
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
