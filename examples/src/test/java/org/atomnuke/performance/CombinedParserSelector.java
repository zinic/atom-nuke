package org.atomnuke.performance;

import java.io.File;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.atomnuke.Nuke;
import org.atomnuke.NukeKernel;
import org.atomnuke.atom.io.reader.sax.SaxAtomReaderFactory;
import org.atomnuke.examples.handler.FeedFileWriterHandler;
import org.atomnuke.examples.listener.ClasspathSource;
import org.atomnuke.examples.listener.eventlet.CounterEventlet;
import org.atomnuke.listener.eps.EventletRelay;
import org.atomnuke.listener.eps.selectors.CategorySelector;
import org.atomnuke.task.AtomTask;
import org.atomnuke.util.TimeValue;

/**
 *
 * @author zinic
 */
public class CombinedParserSelector {

   public static void main(String[] args) throws Exception {
      final AtomicLong events = new AtomicLong(0);

      final EventletRelay relay = new EventletRelay();
      relay.enlistHandler(new FeedFileWriterHandler(new File("/tmp/test.feed")), new CategorySelector(new String[]{"test"}, new String[]{"test"}));
      relay.enlistHandler(new CounterEventlet(events, false));

      final NukeKernel nukeKernel = new NukeKernel();

      final AtomTask task = nukeKernel.follow(new ClasspathSource(new SaxAtomReaderFactory(), "/META-INF/examples/atom/PerformanceTestContents.xml"), new TimeValue(1, TimeUnit.MICROSECONDS));
      task.addListener(relay);

      nukeKernel.start();

      Thread.sleep(10000);

      nukeKernel.destroy();

      System.out.println("Processed " + events.get() + " entry events in ten seconds.");
   }
}
