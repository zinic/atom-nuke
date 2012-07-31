package net.jps.nuke.examples.listener;

import java.util.concurrent.atomic.AtomicLong;
import net.jps.nuke.atom.model.Entry;
import net.jps.nuke.atom.model.Feed;
import net.jps.nuke.listener.AtomListener;
import net.jps.nuke.listener.AtomListenerException;
import net.jps.nuke.listener.AtomListenerResult;
import net.jps.nuke.service.ServiceDestructionException;
import net.jps.nuke.service.ServiceInitializationException;

/**
 *
 * @author zinic
 */
public class EventCounterListener implements AtomListener {

   protected final AtomicLong events;

   public EventCounterListener(AtomicLong events) {
      this.events = events;
   }

   @Override
   public void init() throws ServiceInitializationException {
   }

   @Override
   public void destroy() throws ServiceDestructionException {
   }

   @Override
   public AtomListenerResult entry(Entry entry) throws AtomListenerException {
      events.incrementAndGet();

      return AtomListenerResult.ok();
   }

   @Override
   public AtomListenerResult feedPage(Feed page) throws AtomListenerException {
      events.incrementAndGet();

      return AtomListenerResult.ok();
   }
}
