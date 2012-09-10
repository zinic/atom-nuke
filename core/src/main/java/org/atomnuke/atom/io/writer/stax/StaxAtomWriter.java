package org.atomnuke.atom.io.writer.stax;

import java.io.OutputStream;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.atomnuke.atom.io.AtomWriteException;
import org.atomnuke.atom.io.AtomWriter;
import org.atomnuke.atom.io.cfg.WriterConfiguration;
import org.atomnuke.atom.model.Entry;
import org.atomnuke.atom.model.Feed;

/**
 *
 * @author zinic
 */
public class StaxAtomWriter implements AtomWriter {

   private final WriterConfiguration writerConfiguration;
   private final XMLOutputFactory outputFactory;

   public StaxAtomWriter(WriterConfiguration writerConfiguration, XMLOutputFactory outputFactory) {
      this.writerConfiguration = writerConfiguration;
      this.outputFactory = outputFactory;
   }

   @Override
   public void write(OutputStream output, Feed f) throws AtomWriteException {
      try {
         final XMLStreamWriter writer = outputFactory.createXMLStreamWriter(output);
         Writer.instance().write(new WriterContext(writer, writerConfiguration), f);
      } catch (XMLStreamException xmlse) {
         throw new AtomWriteException("Failed to write Atom entry. Reason: " + xmlse.getMessage(), xmlse);
      }
   }

   @Override
   public void write(OutputStream output, Entry e) throws AtomWriteException {
      try {
         final XMLStreamWriter writer = outputFactory.createXMLStreamWriter(output);
         Writer.instance().write(new WriterContext(writer, writerConfiguration), e);
      } catch (XMLStreamException xmlse) {
         throw new AtomWriteException("Failed to write Atom entry. Reason: " + xmlse.getMessage(), xmlse);
      }
   }
}
