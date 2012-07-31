package net.jps.nuke.examples;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import net.jps.nuke.Nuke;
import net.jps.nuke.NukeKernel;
import net.jps.nuke.examples.listener.PrintStreamOutputListener;
import net.jps.nuke.examples.source.EventGenerator;
import net.jps.nuke.listener.eps.Relay;
import net.jps.nuke.task.Task;
import net.jps.nuke.util.TimeValue;

/**
 *
 * @author zinic
 */
public class EventGeneratorMain {

   public static void main(String[] args) throws Exception {
      final Nuke nukeKernel = new NukeKernel();
      final AtomicLong eventsProcessed = new AtomicLong(0);

      for (int taskId = 1; taskId <= 30; taskId++) {
         final Task task = nukeKernel.follow(new EventGenerator("Task " + taskId, true), new TimeValue(100 * taskId, TimeUnit.NANOSECONDS));
         final Relay relay = new Relay();

         relay.enlistHandler(new PrintStreamOutputListener(System.out, "Task " + taskId + " - Listener 1", eventsProcessed));
         relay.enlistHandler(new PrintStreamOutputListener(System.out, "Task " + taskId + " - Listener 2", eventsProcessed));
         relay.enlistHandler(new PrintStreamOutputListener(System.out, "Task " + taskId + " - Listener 3", eventsProcessed));
         relay.enlistHandler(new PrintStreamOutputListener(System.out, "Task " + taskId + " - Listener 4", eventsProcessed));
         relay.enlistHandler(new PrintStreamOutputListener(System.out, "Task " + taskId + " - Listener 5", eventsProcessed));
      }

      nukeKernel.start();

      Thread.sleep(10000);

      nukeKernel.destroy();
   }
}
