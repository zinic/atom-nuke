package org.atomnuke.atom;

import java.io.InputStream;
import org.atomnuke.atom.io.AtomReader;

/**
 * @deprecated This class has been deprecated in favor of the
 * @see AtomReader
 * 
 * @author zinic
 */
@Deprecated
public interface Reader {

   ParserResult read(final InputStream source) throws AtomParserException;
}
