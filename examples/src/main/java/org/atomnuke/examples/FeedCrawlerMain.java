package org.atomnuke.examples;

import java.util.concurrent.TimeUnit;
import org.atomnuke.NukeKernel;
import org.atomnuke.atom.model.Entry;
import org.atomnuke.atom.model.builder.CategoryBuilder;
import org.atomnuke.sink.eps.EventletChainSink;
import org.atomnuke.sink.eps.eventlet.AtomEventlet;
import org.atomnuke.sink.eps.eventlet.AtomEventletException;
import org.atomnuke.sink.eps.eventlet.AtomEventletPartial;
import org.atomnuke.sink.selectors.CategorySelector;
import org.atomnuke.sink.selectors.CategorySelectorImpl;
import org.atomnuke.source.crawler.FeedCrawlerSourceFactory;
import org.atomnuke.task.atom.AtomTask;
import org.atomnuke.task.context.AtomTaskContext;
import org.atomnuke.util.lifecycle.InitializationException;
import org.atomnuke.util.TimeValue;

/**
 * This is an example of using the nuke feed crawler as an embedded library.
 *
 * @author zinic
 */
public class FeedCrawlerMain {

   public static void main(String[] args) throws Exception {
      /*
       * Create a new instance of the Nuke kernel. The kernel is not started by default.
       */
      final NukeKernel nuke = new NukeKernel();

      /*
       * Using a feed crawler factory simplifies creating feed crawlers
       *
       * You can specify a number of options as constructor parameters to the
       * factory. Setting the state directory is usualy all you usually need to
       * consider.
       *
       * new FeedCrawlerSourceFactory(new File("/path/to/my/state/dir"))
       *
       */

      final FeedCrawlerSourceFactory feedCrawlerFactory = new FeedCrawlerSourceFactory();

      /*
       * Nuke tasks represents the poller assigned to the new source that nuke
       * has been asked to follow.
       *
       * The polling intervale supports NANOSECOND to DAY grainularity.
       */
      final AtomTask crawlerTask = nuke.follow(feedCrawlerFactory.newCrawlerSource("crawler-name", "http://feed.domain/feed/"), new TimeValue(1, TimeUnit.MINUTES));

      /*
       * Nuke has a smart way of turning feed pages into individual events
       * through a provided event Sink called a Relay.
       */
      final EventletChainSink eventRelay = new EventletChainSink();

      /*
       * Register the relay to the source task. This allows the relay to recieve
       * events from the feed crawler.
       */
      crawlerTask.addSink(eventRelay);

      /*
       * Event relays may transmit ATOM entry events to special Sinks called
       * eventlets.
       */
      eventRelay.enlistHandler(new AtomEventlet() {
         @Override
         public void entry(Entry entry) throws AtomEventletException {
            System.out.println("Eventlet 1 - New event: " + entry.id().toString());
         }

         @Override
         public void init(AtomTaskContext tc) throws InitializationException {
            System.out.println("Example eventlet initialized.");
         }

         @Override
         public void destroy() {
            System.out.println("Example eventlet destroyed.");
         }
      });

      /*
       * For ease of use, an abstract class is provided for writing simple
       * eventlets.
       */
      eventRelay.enlistHandler(new AtomEventletPartial() {
         @Override
         public void entry(Entry entry) throws AtomEventletException {
            System.out.println("Eventlet 2 - New event: " + entry.id().toString());
         }
      });

      /*
       * Eventlets may opt to trigger on an event through the use of
       * a companion object called a Selector.
       *
       * The default selector: DefaultSelector.instance() selects all events
       * and is set when no selector is provided with an eventlet.
       *
       * The example below uses a provided selector that will filter events
       * based on the entry or feed and entry category terms. Only events that
       * have the desired category terms will be passed to the eventlet.
       */

      final CategorySelector selector1 = new CategorySelectorImpl();
      selector1.addCategory(new CategoryBuilder().setTerm("desired-entry-category").build());

      eventRelay.enlistHandler(new AtomEventletPartial() {
         @Override
         public void entry(Entry entry) throws AtomEventletException {
            System.out.println("Eventlet 3 - New event: " + entry.id().toString());
         }
      }, selector1);

      /*
       * Start the nuke kernel.
       */
      nuke.start();

      /*
       * Sleep for some meaningful reason. The main thread can end and nuke will
       * continue to execute.
       *
       * Nuke has a shutdown hook, however relying on this system to gracefully
       * destroy all running tasks is not recommended. It is ideal for a
       * programmer to destroy the kernel as part of their program's life-cycle.
       */
      Thread.sleep(100000);

      /*
       * Destroying the nuke kernel will halt task scheduling and begin the
       * shutdown life-cycle of every task, Sink and eventlet.
       *
       * This method blocks until all of the tasks have been destroyed.
       */
      nuke.destroy();
   }
}
