package net.jps.nuke.atom.stax;

import net.jps.nuke.atom.WriterConfiguration;
import net.jps.nuke.atom.Writer;
import java.io.OutputStream;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import net.jps.nuke.atom.model.Entry;
import net.jps.nuke.atom.model.Feed;

/**
 *
 * @author zinic
 */
public class StaxAtomWriter implements Writer {

   private static final WriterConfiguration DEFAULT_CONFIGURATION = new WriterConfiguration(WriterConfiguration.NamespaceLevel.NONE);
   
   private final XMLOutputFactory outputFactory;

   public StaxAtomWriter() {
      this(XMLOutputFactory.newFactory());
   }

   public StaxAtomWriter(XMLOutputFactory outputFactory) {
      this.outputFactory = outputFactory;
   }

   public void write(OutputStream output, Feed f) throws XMLStreamException {
      write(output, f, DEFAULT_CONFIGURATION);
   }

   public void write(OutputStream output, Entry e) throws XMLStreamException {
      write(output, e, DEFAULT_CONFIGURATION);
   }

   public void write(OutputStream output, Feed f, WriterConfiguration configuration) throws XMLStreamException {
      final XMLStreamWriter writer = outputFactory.createXMLStreamWriter(output);
      AtomWriter.instance().write(new WriterContext(writer, configuration), f);
   }

   public void write(OutputStream output, Entry e, WriterConfiguration configuration) throws XMLStreamException {
      final XMLStreamWriter writer = outputFactory.createXMLStreamWriter(output);

      AtomWriter.instance().write(new WriterContext(writer, configuration), e);
   }
}
