package org.atomnuke.atom.io;

import org.atomnuke.atom.io.writer.impl.stax.Writer;

/**
 *
 * @author zinic
 */
public interface AtomWriterFactory {

   Writer getInstance();
}
