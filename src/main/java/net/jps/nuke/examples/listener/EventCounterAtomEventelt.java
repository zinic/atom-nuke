package net.jps.nuke.examples.listener;

import java.util.concurrent.atomic.AtomicLong;
import net.jps.nuke.atom.model.Entry;
import net.jps.nuke.listener.eps.eventlet.AtomEventletException;
import net.jps.nuke.listener.eps.eventlet.AtomEventlet;
import net.jps.nuke.service.DestructionException;
import net.jps.nuke.service.InitializationException;

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
   public void init() throws InitializationException {
   }

   @Override
   public void destroy() throws DestructionException {
   }

   @Override
   public void entry(Entry entry) throws AtomEventletException {
      entryEvents.incrementAndGet();
   }
}
