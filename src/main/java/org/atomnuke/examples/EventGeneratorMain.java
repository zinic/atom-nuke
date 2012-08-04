package org.atomnuke.examples;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.atomnuke.nuke.Nuke;
import org.atomnuke.nuke.NukeKernel;
import org.atomnuke.examples.listener.PrintStreamOutputListener;
import org.atomnuke.examples.source.EventGenerator;
import org.atomnuke.listener.eps.Relay;
import org.atomnuke.task.Task;
import org.atomnuke.util.TimeValue;

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
