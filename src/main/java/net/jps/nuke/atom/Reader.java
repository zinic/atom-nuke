package net.jps.nuke.atom;

import java.io.InputStream;

/**
 *
 * @author zinic
 */
public interface Reader {

   Result read(final InputStream source) throws AtomParserException;
}
