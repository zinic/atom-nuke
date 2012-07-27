package net.jps.nuke.listener;

import net.jps.nuke.atom.Reader;
import net.jps.nuke.atom.Writer;

/**
 *
 * @author zinic
 */
public interface FeedListenerFactory {

   AtomListener newListener(Reader reader, Writer writer);
}
