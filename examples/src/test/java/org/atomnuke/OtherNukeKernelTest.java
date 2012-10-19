package org.atomnuke;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.atomnuke.examples.listener.eventlet.CounterEventlet;
import org.atomnuke.examples.source.EventGenerator;
import org.atomnuke.listener.eps.EventletRelay;
import org.atomnuke.task.AtomTask;
import org.atomnuke.util.TimeValue;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author zinic
 */
public class OtherNukeKernelTest {

   @Test @Ignore
   public void nukeShakedownTest() throws Exception {
      final Nuke nukeKernel = new NukeKernel(1, 1);
      final AtomicLong eventsProcessed = new AtomicLong(0);

      final AtomTask task = nukeKernel.follow(new EventGenerator("Task 1", true), new TimeValue(1, TimeUnit.SECONDS));

      final EventletRelay relay = new EventletRelay();
      relay.enlistHandler(new CounterEventlet(eventsProcessed, false));

      task.addListener(relay);

      nukeKernel.start();

      Thread.sleep(10000);

      task.handle().cancellationRemote().cancel();

      Thread.sleep(10000);

      nukeKernel.destroy();

      System.out.println("Processed " + eventsProcessed.get() + " events in one second.");
   }
}
