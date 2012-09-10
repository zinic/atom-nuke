package org.atomnuke.atom.io;

import java.io.OutputStream;
import org.atomnuke.atom.model.Entry;
import org.atomnuke.atom.model.Feed;

/**
 *
 * @author zinic
 */
public interface AtomWriter {

   void write(OutputStream output, Feed f) throws AtomWriteException;

   void write(OutputStream output, Entry e) throws AtomWriteException;
}
