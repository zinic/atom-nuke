package org.atomnuke.atom.io;

import java.io.OutputStream;
import org.atomnuke.atom.model.Entry;
import org.atomnuke.atom.model.Feed;

/**
 * An AtomWriter is an abstraction for writing ATOM models to output streams.
 *
 * @author zinic
 */
public interface AtomWriter {

   /**
    * Writes the given feed object to the given output stream.
    *
    * @param output output stream to write this writer's representation to.
    * @param f feed object to write.
    * @throws AtomWriteException thrown when a failure occurs during writing.
    * This may wrap an IOException.
    */
   void write(OutputStream output, Feed f) throws AtomWriteException;

   /**
    * Writes the given entry object to the given output stream.
    *
    * @param output output stream to write this writer's representation to.
    * @param e entry object to write.
    * @throws AtomWriteException thrown when a failure occurs during writing.
    * This may wrap an IOException.
    */
   void write(OutputStream output, Entry e) throws AtomWriteException;
}
