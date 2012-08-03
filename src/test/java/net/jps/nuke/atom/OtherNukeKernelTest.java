package net.jps.nuke.atom;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import net.jps.nuke.Nuke;
import net.jps.nuke.NukeKernel;
import net.jps.nuke.examples.listener.EventCounterAtomEventelt;
import net.jps.nuke.examples.source.EventGenerator;
import net.jps.nuke.listener.eps.ReentrantRelay;
import net.jps.nuke.listener.eps.Relay;
import net.jps.nuke.task.Task;
import net.jps.nuke.util.TimeValue;
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
