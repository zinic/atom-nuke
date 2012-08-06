package org.atomnuke.examples.listener;

import java.util.concurrent.atomic.AtomicLong;
import org.atomnuke.atom.model.Entry;
import org.atomnuke.listener.eps.eventlet.AtomEventlet;
import org.atomnuke.listener.eps.eventlet.AtomEventletException;
import org.atomnuke.task.context.TaskContext;
import org.atomnuke.task.lifecycle.DestructionException;
import org.atomnuke.task.lifecycle.InitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class CounterEventlet implements AtomEventlet {

   private static final Logger LOG = LoggerFactory.getLogger(CounterEventlet.class);
   
   protected final AtomicLong entryEvents;

   public CounterEventlet() {
      this(new AtomicLong());
   }

   public CounterEventlet(AtomicLong entryEvents) {
      this.entryEvents = entryEvents;
   }

   @Override
   public void init(TaskContext tc) throws InitializationException {
   }

   @Override
   public void destroy(TaskContext tc) throws DestructionException {
      LOG.info("Processed " + entryEvents.toString() + " events.");
   }

   @Override
   public void entry(Entry entry) throws AtomEventletException {
      entryEvents.incrementAndGet();
   }
}
