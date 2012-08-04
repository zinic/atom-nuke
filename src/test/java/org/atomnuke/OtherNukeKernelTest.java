package org.atomnuke;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.atomnuke.examples.listener.EventCounterAtomEventelt;
import org.atomnuke.examples.source.EventGenerator;
import org.atomnuke.listener.eps.ReentrantRelay;
import org.atomnuke.listener.eps.Relay;
import org.atomnuke.task.Task;
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

      final Task task = nukeKernel.follow(new EventGenerator("Task 1", true), new TimeValue(1, TimeUnit.SECONDS));

      final Relay relay = new ReentrantRelay();
      relay.enlistHandler(new EventCounterAtomEventelt(eventsProcessed));

      task.addListener(relay);

      nukeKernel.start();
      
      Thread.sleep(10000);

      task.cancel();
      
      Thread.sleep(10000);

      nukeKernel.destroy();

      System.out.println("Processed " + eventsProcessed.get() + " events in one second.");
   }
}
