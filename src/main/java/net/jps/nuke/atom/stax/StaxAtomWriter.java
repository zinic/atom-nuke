package net.jps.nuke.atom.stax;

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

   private final XMLOutputFactory outputFactory;

   public StaxAtomWriter() {
      this(XMLOutputFactory.newFactory());
   }

   public StaxAtomWriter(XMLOutputFactory outputFactory) {
      this.outputFactory = outputFactory;
   }

   public void write(OutputStream output, Feed f) throws XMLStreamException {
      final XMLStreamWriter writer = outputFactory.createXMLStreamWriter(output);
      AtomWriter.instance().write(writer, f);
   }

   public void write(OutputStream output, Entry e) throws XMLStreamException {
      final XMLStreamWriter writer = outputFactory.createXMLStreamWriter(output);
      AtomWriter.instance().write(writer, e);
   }
}
