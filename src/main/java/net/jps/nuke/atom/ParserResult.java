package net.jps.nuke.atom;

import java.net.URI;

/**
 *
 * @author zinic
 */
public interface ParserResult {

   FeedMetadata getFeedMetadata();

   URI getNextLocation();
}
