package net.jps.nuke.examples.listener;

import java.io.PrintStream;
import java.util.concurrent.atomic.AtomicLong;
import net.jps.nuke.atom.model.Entry;
import net.jps.nuke.listener.eps.eventlet.AtomEventletException;
import net.jps.nuke.task.context.TaskContext;
import net.jps.nuke.task.lifecycle.DestructionException;
import net.jps.nuke.task.lifecycle.InitializationException;

/**
 *
 * @author zinic
 */
public class PrintStreamOutputListener extends EventCounterAtomEventelt {

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
   public void init(TaskContext tc) throws InitializationException {
      out.println("PrintStreamOutputListener(" + toString() + ") initalized.");
   }

   @Override
   public void destroy(TaskContext tc) throws DestructionException {
      out.println("PrintStreamOutputListener(" + toString() + ") destroyed.");
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
