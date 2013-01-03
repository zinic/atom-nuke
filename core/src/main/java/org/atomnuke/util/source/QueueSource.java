package org.atomnuke.util.source;

import org.atomnuke.source.result.AtomSourceResult;
import org.atomnuke.atom.model.Entry;
import org.atomnuke.source.AtomSourceException;

/**
 *
 * @author zinic
 */
public interface QueueSource {

   void put(Entry e);
   
   AtomSourceResult poll() throws AtomSourceException;
}
