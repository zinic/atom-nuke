package net.jps.nuke.examples;

import java.util.concurrent.TimeUnit;
import net.jps.nuke.Nuke;
import net.jps.nuke.NukeKernel;
import net.jps.nuke.examples.listener.test.PrintStreamOutputListener;
import net.jps.nuke.examples.source.EventGenerator;
import net.jps.nuke.task.Task;
import net.jps.nuke.util.TimeValue;

/**
 *
 * @author zinic
 */
public class EventGeneratorMain {

    public static void main(String[] args) throws InterruptedException {
        final Nuke nukeInstance = new NukeKernel();

        for (int taskId = 1; taskId <= 30; taskId++) {
            final Task task = nukeInstance.follow(new EventGenerator("Task " + taskId, true), new TimeValue(1000 * taskId, TimeUnit.NANOSECONDS));

            task.addListener(new PrintStreamOutputListener(System.out, "Task " + taskId + " - Listener 1"));
            task.addListener(new PrintStreamOutputListener(System.out, "Task " + taskId + " - Listener 2"));
            task.addListener(new PrintStreamOutputListener(System.out, "Task " + taskId + " - Listener 3"));
            task.addListener(new PrintStreamOutputListener(System.out, "Task " + taskId + " - Listener 4"));
            task.addListener(new PrintStreamOutputListener(System.out, "Task " + taskId + " - Listener 5"));
        }

        nukeInstance.start();
        
        Thread.sleep(50000);
    }
}
