package net.jps.nuke.atom;

import java.io.InputStream;

/**
 *
 * @author zinic
 */
public interface FeedParser {
   
   ParserResult read(InputStream in) throws AtomParserException;
}
