package net.jps.nuke.examples.listener;

import java.io.PrintStream;
import java.util.concurrent.atomic.AtomicLong;
import net.jps.nuke.atom.model.Entry;
import net.jps.nuke.atom.model.Feed;
import net.jps.nuke.listener.AtomListenerException;
import net.jps.nuke.listener.AtomListenerResult;
import net.jps.nuke.service.ServiceDestructionException;
import net.jps.nuke.service.ServiceInitializationException;

/**
 *
 * @author zinic
 */
public class PrintStreamOutputListener extends EventCounterListener {

   private final PrintStream out;
   private final String msg;
   private final long creationTime;

   public PrintStreamOutputListener(PrintStream out, String msg, AtomicLong events) {
      super(events);

      this.out = out;
      this.msg = msg;
      this.creationTime = System.currentTimeMillis();
   }

   @Override
   public void init() throws ServiceInitializationException {
      out.println("PrintStreamOutputListener(" + toString() + ") initalized.");
   }

   @Override
   public void destroy() throws ServiceDestructionException {
      out.println("PrintStreamOutputListener(" + toString() + ") destroyed.");
   }

   private void newEvent() {
      final long eventsCaught = events.incrementAndGet();
      final long nowInMillis = System.currentTimeMillis();

      if (eventsCaught % 10000 == 0) {
         out.println((nowInMillis - creationTime) + "ms elapsed. Events received: " + eventsCaught + " - Events per 10ms: " + (eventsCaught / ((nowInMillis - creationTime) / 10)) + " - (" + msg + ")");
      }
   }

   @Override
   public AtomListenerResult entry(Entry entry) throws AtomListenerException {
      newEvent();

      return AtomListenerResult.ok();
   }

   @Override
   public AtomListenerResult feedPage(Feed page) throws AtomListenerException {
      newEvent();

      return AtomListenerResult.ok();
   }
}
