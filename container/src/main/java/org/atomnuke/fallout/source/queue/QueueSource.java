package org.atomnuke.fallout.source.queue;

import org.atomnuke.atom.model.Entry;
import org.atomnuke.source.AtomSource;

/**
 *
 * @author zinic
 */
public interface QueueSource extends AtomSource {

   void put(Entry e);
}
