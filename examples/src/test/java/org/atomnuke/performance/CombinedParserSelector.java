package org.atomnuke.performance;

import java.io.File;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.atomnuke.Nuke;
import org.atomnuke.NukeKernel;
import org.atomnuke.examples.handler.FeedFileWriterHandler;
import org.atomnuke.examples.listener.ClasspathSource;
import org.atomnuke.examples.listener.eventlet.CounterEventlet;
import org.atomnuke.listener.eps.Relay;
import org.atomnuke.listener.eps.selectors.CategorySelector;
import org.atomnuke.task.Task;
import org.atomnuke.util.TimeValue;

/**
 *
 * @author zinic
 */
public class CombinedParserSelector {

   public static void main(String[] args) throws Exception {
      final AtomicLong events = new AtomicLong(0);

      final Relay relay = new Relay();
      relay.enlistHandler(new FeedFileWriterHandler(new File("/tmp/test.feed")), new CategorySelector(new String[]{"test"}, new String[]{"test"}));
      relay.enlistHandler(new CounterEventlet(events, false));

      final Nuke nukeKernel = new NukeKernel();

      final Task task = nukeKernel.follow(new ClasspathSource("/META-INF/examples/atom/PerformanceTestContents.xml"), new TimeValue(1, TimeUnit.MICROSECONDS));
      task.addListener(relay);

      nukeKernel.start();

      Thread.sleep(10000);

      nukeKernel.destroy();

      System.out.println("Processed " + events.get() + " entry events in ten seconds.");
   }
}
