package net.jps.nuke.atom.performance;

import java.io.File;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import net.jps.nuke.Nuke;
import net.jps.nuke.NukeKernel;
import net.jps.nuke.examples.handler.FeedFileWriterHandler;
import net.jps.nuke.examples.listener.ClasspathSource;
import net.jps.nuke.examples.listener.EventCounterAtomEventelt;
import net.jps.nuke.listener.eps.ReentrantRelay;
import net.jps.nuke.listener.eps.Relay;
import net.jps.nuke.listener.eps.selectors.CategorySelector;
import net.jps.nuke.task.Task;
import net.jps.nuke.util.TimeValue;

/**
 *
 * @author zinic
 */
public class CombinedParserSelector {

   public static void main(String[] args) throws Exception {
      final AtomicLong events = new AtomicLong(0);

      final Relay relay = new ReentrantRelay();
      relay.enlistHandler(new FeedFileWriterHandler(new File("/tmp/test.feed")), new CategorySelector(new String[]{"test"}, new String[]{"test"}));
      relay.enlistHandler(new EventCounterAtomEventelt(events));
      
      final Nuke nukeKernel = new NukeKernel();

      final Task task = nukeKernel.follow(new ClasspathSource("/META-INF/examples/atom/PerformanceTestContents.xml"), new TimeValue(1, TimeUnit.NANOSECONDS));
      task.addListener(relay);

      nukeKernel.start();

      Thread.sleep(10000);

      nukeKernel.destroy();
      
      System.out.println("Processed " + events.get() + " entry events in ten seconds.");
   }
}
