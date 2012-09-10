package org.atomnuke.atom.io;

import java.io.OutputStream;
import javax.xml.stream.XMLStreamException;
import org.atomnuke.atom.io.AtomWriteException;
import org.atomnuke.atom.io.writer.impl.stax.StaxWriterConfiguration;
import org.atomnuke.atom.model.Entry;
import org.atomnuke.atom.model.Feed;

/**
 *
 * @author zinic
 */
public interface AtomWriter {

   void write(OutputStream output, Feed f) throws AtomWriteException;

   void write(OutputStream output, Entry e) throws AtomWriteException;

   void write(OutputStream output, Feed f, StaxWriterConfiguration configuration) throws AtomWriteException;

   void write(OutputStream output, Entry e, StaxWriterConfiguration configuration) throws AtomWriteException;
}
