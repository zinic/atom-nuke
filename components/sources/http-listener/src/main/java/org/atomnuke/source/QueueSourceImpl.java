package org.atomnuke.source;

import java.util.LinkedList;
import java.util.Queue;
import org.atomnuke.atom.model.Entry;
import org.atomnuke.source.impl.AtomSourceResultImpl;

/**
 *
 * @author zinic
 */
public class QueueSourceImpl implements QueueSource {

   private final Queue<Entry> entryQueue;

   public QueueSourceImpl() {
      entryQueue = new LinkedList<Entry>();
   }

   @Override
   public synchronized void put(Entry e) {
      entryQueue.add(e);
   }

   @Override
   public synchronized AtomSourceResult poll() throws AtomSourceException {
      final Entry nextEntry = entryQueue.poll();

      return new AtomSourceResultImpl(nextEntry);
   }
}
