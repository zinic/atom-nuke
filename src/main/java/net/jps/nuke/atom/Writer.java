package net.jps.nuke.atom;

import java.io.OutputStream;
import javax.xml.stream.XMLStreamException;
import net.jps.nuke.atom.model.Entry;
import net.jps.nuke.atom.model.Feed;

/**
 *
 * @author zinic
 */
public interface Writer {

   public void write(OutputStream output, Feed f) throws XMLStreamException;

   public void write(OutputStream output, Entry e) throws XMLStreamException;
}
