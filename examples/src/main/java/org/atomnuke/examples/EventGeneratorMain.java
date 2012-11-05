package org.atomnuke.examples;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.atomnuke.Nuke;
import org.atomnuke.NukeKernel;
import org.atomnuke.examples.eventlets.PrintStreamEventlet;
import org.atomnuke.examples.source.EventGenerator;
import org.atomnuke.sink.eps.EventletChainSink;
import org.atomnuke.task.atom.AtomTask;
import org.atomnuke.util.TimeValue;

/**
 *
 * @author zinic
 */
public class EventGeneratorMain {

   public static void main(String[] args) throws Exception {
      final NukeKernel nukeKernel = new NukeKernel();
      final AtomicLong eventsProcessed = new AtomicLong(0);

      for (int taskId = 1; taskId <= 30; taskId++) {
         final AtomTask task = nukeKernel.follow(new EventGenerator("Task " + taskId, true), new TimeValue(100 * taskId, TimeUnit.NANOSECONDS));
         final EventletChainSink relay = new EventletChainSink();

         relay.enlistHandler(new PrintStreamEventlet(System.out, "Task " + taskId + " - Sink 1", eventsProcessed));
         relay.enlistHandler(new PrintStreamEventlet(System.out, "Task " + taskId + " - Sink 2", eventsProcessed));
         relay.enlistHandler(new PrintStreamEventlet(System.out, "Task " + taskId + " - Sink 3", eventsProcessed));
         relay.enlistHandler(new PrintStreamEventlet(System.out, "Task " + taskId + " - Sink 4", eventsProcessed));
         relay.enlistHandler(new PrintStreamEventlet(System.out, "Task " + taskId + " - Sink 5", eventsProcessed));
      }

      nukeKernel.start();

      Thread.sleep(10000);

      nukeKernel.destroy();
   }
}
