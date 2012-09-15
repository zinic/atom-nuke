package org.atomnuke.source;

import org.atomnuke.atom.model.Entry;

/**
 *
 * @author zinic
 */
public interface QueueSource {

   void put(Entry e);

   AtomSourceResult poll() throws AtomSourceException;
}
