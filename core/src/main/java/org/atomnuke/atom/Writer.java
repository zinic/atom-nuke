package org.atomnuke.atom;

import java.io.OutputStream;
import javax.xml.stream.XMLStreamException;
import org.atomnuke.atom.io.AtomWriter;
import org.atomnuke.atom.model.Entry;
import org.atomnuke.atom.model.Feed;

/**
 * @deprecated This class has been deprecated in favor of the more flexible io implementation
 * @see AtomWriter
 * 
 * @author zinic
 */
public interface Writer {

   void write(OutputStream output, Feed f) throws XMLStreamException;

   void write(OutputStream output, Entry e) throws XMLStreamException;

   void write(OutputStream output, Feed f, WriterConfiguration configuration) throws XMLStreamException;

   void write(OutputStream output, Entry e, WriterConfiguration configuration) throws XMLStreamException;
}
