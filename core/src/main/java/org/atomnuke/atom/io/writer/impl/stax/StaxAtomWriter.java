package org.atomnuke.atom.io.writer.impl.stax;

import java.io.OutputStream;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.atomnuke.atom.io.AtomWriteException;
import org.atomnuke.atom.io.AtomWriter;
import org.atomnuke.atom.io.cfg.NamespaceLevel;
import org.atomnuke.atom.model.Entry;
import org.atomnuke.atom.model.Feed;

/**
 *
 * @author zinic
 */
public class StaxAtomWriter implements AtomWriter {

   private static final StaxWriterConfiguration DEFAULT_CONFIGURATION = new StaxWriterConfiguration(NamespaceLevel.NONE);
   
   private final XMLOutputFactory outputFactory;

   public StaxAtomWriter() {
      this(XMLOutputFactory.newFactory());
   }

   public StaxAtomWriter(XMLOutputFactory outputFactory) {
      this.outputFactory = outputFactory;
   }

   @Override
   public void write(OutputStream output, Feed f) throws AtomWriteException {
      write(output, f, DEFAULT_CONFIGURATION);
   }

   @Override
   public void write(OutputStream output, Entry e) throws AtomWriteException {
      write(output, e, DEFAULT_CONFIGURATION);
   }

   @Override
   public void write(OutputStream output, Feed f, StaxWriterConfiguration configuration) throws AtomWriteException {
      try {
         final XMLStreamWriter writer = outputFactory.createXMLStreamWriter(output);
         Writer.instance().write(new WriterContext(writer, configuration), f);
      } catch (XMLStreamException xmlse) {
         throw new AtomWriteException("Failed to write Atom entry. Reason: " + xmlse.getMessage(), xmlse);
      }
   }

   @Override
   public void write(OutputStream output, Entry e, StaxWriterConfiguration configuration) throws AtomWriteException {
      try {
         final XMLStreamWriter writer = outputFactory.createXMLStreamWriter(output);
         Writer.instance().write(new WriterContext(writer, configuration), e);
      } catch (XMLStreamException xmlse) {
         throw new AtomWriteException("Failed to write Atom entry. Reason: " + xmlse.getMessage(), xmlse);
      }
   }
}
