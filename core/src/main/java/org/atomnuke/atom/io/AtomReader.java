package org.atomnuke.atom.io;

import java.io.InputStream;

/**
 * An AtomReader is an abstraction for reading ATOM models from input streams.
 *
 * @author zinic
 */
public interface AtomReader {

   /**
    * Reads an ATOM model from the input stream by using this reader's
    * formatting.
    *
    * @param source the input stream to read ATOM data from.
    * @return a result that may contain either an entry or a feed object along
    * with the necessary information to discover this.
    * @throws AtomReadException thrown when a failure occurs during reading.
    * This may wrap an IOException.
    */
   ReaderResult read(final InputStream source) throws AtomReadException;
}
