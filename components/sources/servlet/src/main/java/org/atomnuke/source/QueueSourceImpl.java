package org.atomnuke.source;

import java.util.LinkedList;
import java.util.Queue;
import org.atomnuke.atom.model.Entry;
import org.atomnuke.source.impl.AtomSourceResultImpl;
import org.atomnuke.task.context.TaskContext;
import org.atomnuke.task.lifecycle.DestructionException;
import org.atomnuke.task.lifecycle.InitializationException;

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
   public void init(TaskContext tc) throws InitializationException {
   }

   @Override
   public void destroy(TaskContext tc) throws DestructionException {
   }

   @Override
   public synchronized void put(Entry e) {
      entryQueue.add(e);
   }

   @Override
   public synchronized AtomSourceResult poll() throws AtomSourceException {
      final Entry nextEntry = entryQueue.poll();

      return nextEntry != null ? new AtomSourceResultImpl(nextEntry) : null;
   }
}
