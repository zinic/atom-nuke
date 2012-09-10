package org.atomnuke.atom.io;

import java.io.InputStream;

/**
 *
 * @author zinic
 */
public interface AtomReader {

   ReaderResult read(final InputStream source) throws AtomReadException;
}
