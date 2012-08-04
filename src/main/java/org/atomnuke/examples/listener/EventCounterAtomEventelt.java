package org.atomnuke.examples.listener;

import java.util.concurrent.atomic.AtomicLong;
import org.atomnuke.atom.model.Entry;
import org.atomnuke.listener.eps.eventlet.AtomEventlet;
import org.atomnuke.listener.eps.eventlet.AtomEventletException;
import org.atomnuke.task.context.TaskContext;
import org.atomnuke.task.lifecycle.DestructionException;
import org.atomnuke.task.lifecycle.InitializationException;

/**
 *
 * @author zinic
 */
public class EventCounterAtomEventelt implements AtomEventlet {

   protected final AtomicLong entryEvents;

   public EventCounterAtomEventelt(AtomicLong entryEvents) {
      this.entryEvents = entryEvents;
   }

   @Override
   public void init(TaskContext tc) throws InitializationException {
   }

   @Override
   public void destroy(TaskContext tc) throws DestructionException {
   }

   @Override
   public void entry(Entry entry) throws AtomEventletException {
      entryEvents.incrementAndGet();
   }
}
