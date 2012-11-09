package org.atomnuke.examples.eventlets;

import java.util.concurrent.atomic.AtomicLong;
import org.atomnuke.atom.model.Entry;
import org.atomnuke.sink.eps.eventlet.AtomEventlet;
import org.atomnuke.sink.eps.eventlet.AtomEventletException;
import org.atomnuke.task.context.AtomTaskContext;
import org.atomnuke.lifecycle.DestructionException;
import org.atomnuke.lifecycle.InitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class CounterEventlet implements AtomEventlet {

   private static final Logger LOG = LoggerFactory.getLogger(CounterEventlet.class);

   protected final AtomicLong entryEvents;
   private boolean log;

   public CounterEventlet() {
      this(new AtomicLong(), true);
   }

   public CounterEventlet(AtomicLong entryEvents, boolean log) {
      this.entryEvents = entryEvents;
      this.log = log;
   }

   @Override
   public void init(AtomTaskContext tc) throws InitializationException {
   }

   @Override
   public void destroy() {
      if (log) {
         LOG.info("Processed " + entryEvents.toString() + " events.");
      }
   }

   @Override
   public void entry(Entry entry) throws AtomEventletException {
      final long events = entryEvents.incrementAndGet();

      if (log && events % 1000 == 0) {
         LOG.info("Processed " + events + " events.");
      }
   }
}
