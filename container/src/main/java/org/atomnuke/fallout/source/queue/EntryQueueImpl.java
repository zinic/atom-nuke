package org.atomnuke.fallout.source.queue;

import org.atomnuke.source.result.AtomSourceResult;
import java.util.LinkedList;
import java.util.Queue;
import org.atomnuke.atom.model.Entry;
import org.atomnuke.source.AtomSourceException;
import org.atomnuke.source.result.AtomSourceResultImpl;
import org.atomnuke.task.context.AtomTaskContext;
import org.atomnuke.lifecycle.InitializationException;
import org.atomnuke.source.action.ActionType;
import org.atomnuke.source.action.AtomSourceActionImpl;

/**
 *
 * @author zinic
 */
public class EntryQueueImpl implements QueueSource {

   private final Queue<Entry> entryQueue;

   public EntryQueueImpl() {
      entryQueue = new LinkedList<Entry>();
   }

   @Override
   public void init(AtomTaskContext contextObject) throws InitializationException {
   }

   @Override
   public synchronized void destroy() {
      entryQueue.clear();
   }

   @Override
   public synchronized void put(Entry e) {
      entryQueue.add(e);
   }

   @Override
   public synchronized AtomSourceResult poll() throws AtomSourceException {
      if (entryQueue.isEmpty()) {
         return new AtomSourceResultImpl(new AtomSourceActionImpl(ActionType.SLEEP));
      }

      return new AtomSourceResultImpl(entryQueue.poll());
   }
}
