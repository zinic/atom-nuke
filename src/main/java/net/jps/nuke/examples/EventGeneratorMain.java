package net.jps.nuke.examples;

import java.util.concurrent.TimeUnit;
import net.jps.nuke.Nuke;
import net.jps.nuke.NukeKernel;
import net.jps.nuke.examples.listener.test.PrintStreamOutputListener;
import net.jps.nuke.source.event.EventGeneratorImpl;
import net.jps.nuke.task.Task;
import net.jps.nuke.util.TimeValue;

/**
 *
 * @author zinic
 */
public class EventGeneratorMain {

   public static void main(String[] args) throws InterruptedException {
      final Nuke nukeInstance = new NukeKernel();
      nukeInstance.start();
      
      final Task task1 = nukeInstance.follow(new EventGeneratorImpl(true), new TimeValue(1, TimeUnit.NANOSECONDS));
      task1.addListener(new PrintStreamOutputListener(System.out, "Task 1 - Listener 1"));

      Thread.sleep(50000);
   }
}
