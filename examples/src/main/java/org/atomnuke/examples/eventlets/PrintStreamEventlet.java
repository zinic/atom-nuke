package org.atomnuke.examples.eventlets;

import java.io.PrintStream;
import java.util.concurrent.atomic.AtomicLong;
import org.atomnuke.atom.model.Entry;
import org.atomnuke.sink.eps.eventlet.AtomEventletException;
import org.atomnuke.task.context.AtomTaskContext;
import org.atomnuke.util.lifecycle.DestructionException;
import org.atomnuke.util.lifecycle.InitializationException;

/**
 *
 * @author zinic
 */
public class PrintStreamEventlet extends CounterEventlet {

   private final PrintStream out;
   private final String msg;
   private final long creationTime;

   public PrintStreamEventlet(PrintStream out, String msg, AtomicLong events) {
      super(events, true);

      this.out = out;
      this.msg = msg;
      this.creationTime = System.currentTimeMillis();
   }

   @Override
   public void init(AtomTaskContext tc) throws InitializationException {
      out.println("PrintStreamOutputSink(" + toString() + ") initalized.");
   }

   @Override
   public void destroy() {
      out.println("PrintStreamOutputSink(" + toString() + ") destroyed.");
   }

   private void newEvent() {
      final long eventsCaught = entryEvents.incrementAndGet();
      final long nowInMillis = System.currentTimeMillis();

      if (eventsCaught % 10000 == 0) {
         out.println((nowInMillis - creationTime) + "ms elapsed. Events received: " + eventsCaught + " - Events per 10ms: " + (eventsCaught / ((nowInMillis - creationTime) / 10)) + " - (" + msg + ")");
      }
   }

   @Override
   public void entry(Entry entry) throws AtomEventletException {
      newEvent();
   }
}
