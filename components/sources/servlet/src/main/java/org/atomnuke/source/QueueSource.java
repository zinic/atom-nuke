package org.atomnuke.source;

import org.atomnuke.atom.model.Entry;

/**
 *
 * @author zinic
 */
public interface QueueSource extends AtomSource {

   void put(Entry e);
}
