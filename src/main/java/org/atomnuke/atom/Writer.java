package org.atomnuke.atom;

import java.io.OutputStream;
import javax.xml.stream.XMLStreamException;
import org.atomnuke.atom.model.Entry;
import org.atomnuke.atom.model.Feed;

/**
 *
 * @author zinic
 */
public interface Writer {

   void write(OutputStream output, Feed f) throws XMLStreamException;

   void write(OutputStream output, Entry e) throws XMLStreamException;

   void write(OutputStream output, Feed f, WriterConfiguration configuration) throws XMLStreamException;

   void write(OutputStream output, Entry e, WriterConfiguration configuration) throws XMLStreamException;
}
