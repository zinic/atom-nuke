package org.atomnuke.atom.stax;

import java.io.OutputStream;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.atomnuke.atom.Writer;
import org.atomnuke.atom.WriterConfiguration;
import org.atomnuke.atom.model.Entry;
import org.atomnuke.atom.model.Feed;

/**
 * @deprecated org.atomnuke.atom.io replaces this package
 *
 * @author zinic
 */

@Deprecated
public class StaxAtomWriter implements Writer {

   private static final WriterConfiguration DEFAULT_CONFIGURATION = new WriterConfiguration(WriterConfiguration.NamespaceLevel.NONE);
   private final XMLOutputFactory outputFactory;

   public StaxAtomWriter() {
      this(XMLOutputFactory.newFactory());
   }

   public StaxAtomWriter(XMLOutputFactory outputFactory) {
      this.outputFactory = outputFactory;
   }

   @Override
   public void write(OutputStream output, Feed f) throws XMLStreamException {
      write(output, f, DEFAULT_CONFIGURATION);
   }

   @Override
   public void write(OutputStream output, Entry e) throws XMLStreamException {
      write(output, e, DEFAULT_CONFIGURATION);
   }

   @Override
   public void write(OutputStream output, Feed f, WriterConfiguration configuration) throws XMLStreamException {
      final XMLStreamWriter writer = outputFactory.createXMLStreamWriter(output);
      AtomWriter.instance().write(new WriterContext(writer, configuration), f);
   }

   @Override
   public void write(OutputStream output, Entry e, WriterConfiguration configuration) throws XMLStreamException {
      final XMLStreamWriter writer = outputFactory.createXMLStreamWriter(output);

      AtomWriter.instance().write(new WriterContext(writer, configuration), e);
   }
}
