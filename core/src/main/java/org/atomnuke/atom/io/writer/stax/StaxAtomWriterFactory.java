package org.atomnuke.atom.io.writer.stax;

import javax.xml.stream.XMLOutputFactory;
import org.atomnuke.atom.io.AtomWriter;
import org.atomnuke.atom.io.AtomWriterFactory;
import org.atomnuke.atom.io.cfg.NamespaceLevel;
import org.atomnuke.atom.io.cfg.WriterConfiguration;

/**
 *
 * @author zinic
 */
public class StaxAtomWriterFactory implements AtomWriterFactory {

   private static final WriterConfiguration DEFAULT_CONFIGURATION = new StaxWriterConfiguration(NamespaceLevel.NONE);

   private final WriterConfiguration writerConfiguration;
   private final XMLOutputFactory outputFactory;

   public StaxAtomWriterFactory() {
      this(DEFAULT_CONFIGURATION, XMLOutputFactory.newFactory());
   }

   public StaxAtomWriterFactory(WriterConfiguration writerConfiguration) {
      this(writerConfiguration, XMLOutputFactory.newFactory());
   }

   public StaxAtomWriterFactory(WriterConfiguration writerConfiguration, XMLOutputFactory outputFactory) {
      this.writerConfiguration = writerConfiguration;
      this.outputFactory = outputFactory;
   }

   @Override
   public AtomWriter getInstance() {
      return new StaxAtomWriter(writerConfiguration, outputFactory);
   }
}
