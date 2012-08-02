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
import org.junit.Test;

/**
 *
 * @author zinic
 */
public class NukeKernelTest {

   @Test
   public void nukeShakedownTest() throws Exception {
      final Nuke nukeKernel = new NukeKernel(1, 1);
      final AtomicLong eventsProcessed = new AtomicLong(0);

      for (int taskId = 1; taskId <= 30; taskId++) {
         final Task task = nukeKernel.tasker().follow(new EventGenerator("Task " + taskId, true), new TimeValue(1000 * taskId, TimeUnit.NANOSECONDS));

         final Relay relay = new ReentrantRelay();
         relay.enlistHandler(new EventCounterAtomEventelt(eventsProcessed));
         relay.enlistHandler(new EventCounterAtomEventelt(eventsProcessed));
         relay.enlistHandler(new EventCounterAtomEventelt(eventsProcessed));
         relay.enlistHandler(new EventCounterAtomEventelt(eventsProcessed));
         relay.enlistHandler(new EventCounterAtomEventelt(eventsProcessed));
         
         task.addListener(relay);
      }
      
      nukeKernel.start();

      Thread.sleep(1000);
      
      nukeKernel.destroy();
      
      System.out.println("Processed " + eventsProcessed.get() + " events in one second.");
   }
}
