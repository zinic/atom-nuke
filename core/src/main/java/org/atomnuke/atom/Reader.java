package org.atomnuke.atom;

import java.io.InputStream;

/**
 *
 * @author zinic
 */
public interface Reader {

   ParserResult read(final InputStream source) throws AtomParserException;
}
