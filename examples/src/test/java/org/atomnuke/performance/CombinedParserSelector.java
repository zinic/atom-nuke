package org.atomnuke.performance;

import java.io.File;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.atomnuke.Nuke;
import org.atomnuke.NukeKernel;
import org.atomnuke.atom.io.reader.sax.SaxAtomReaderFactory;
import org.atomnuke.examples.eventlets.FeedFileWriterHandler;
import org.atomnuke.examples.sinks.ClasspathSource;
import org.atomnuke.examples.eventlets.CounterEventlet;
import org.atomnuke.sink.eps.EventletChainSink;
import org.atomnuke.sink.selectors.CategorySelectorImpl;
import org.atomnuke.task.atom.AtomTask;
import org.atomnuke.util.TimeValue;

/**
 *
 * @author zinic
 */
public class CombinedParserSelector {

   public static void main(String[] args) throws Exception {
      final AtomicLong events = new AtomicLong(0);

      final EventletChainSink relay = new EventletChainSink();
      relay.enlistHandler(new FeedFileWriterHandler(new File("/tmp/test.feed")));
      relay.enlistHandler(new CounterEventlet(events, false));

      final NukeKernel nukeKernel = new NukeKernel();

      final AtomTask task = nukeKernel.follow(new ClasspathSource(new SaxAtomReaderFactory(), "/META-INF/examples/atom/PerformanceTestContents.xml"), new TimeValue(1, TimeUnit.MICROSECONDS));
      task.addSink(relay);

      nukeKernel.start();

      Thread.sleep(10000);

      nukeKernel.destroy();

      System.out.println("Processed " + events.get() + " entry events in ten seconds.");
   }
}
