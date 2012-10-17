package org.atomnuke.examples;

import java.util.concurrent.TimeUnit;
import org.atomnuke.Nuke;
import org.atomnuke.NukeKernel;
import org.atomnuke.atom.io.writer.stax.StaxAtomWriterFactory;
import org.atomnuke.task.AtomTask;
import org.atomnuke.examples.listener.HDFSFeedListener;
import org.atomnuke.source.crawler.FeedCrawlerSourceFactory;
import org.atomnuke.util.TimeValue;

/**
 *
 * @author zinic
 */
public class HDFSMain {

   public static void main(String[] args) throws Exception {
      final HDFSFeedListener listener = new HDFSFeedListener("example-1.feed", new StaxAtomWriterFactory());
      final HDFSFeedListener listener2 = new HDFSFeedListener("example-2.feed", new StaxAtomWriterFactory());

      /*
       * Still todo...
       *
       * - Consider using a listener delegation pattern ala SAX
       *    Might not implement this one due to the execution pattern I took. Still
       *    investigating the model.
       *
       */

      // Create the nuke kernel
      final Nuke nuke = new NukeKernel();

      // Start nuke up
      nuke.start();

      // We want to crawl feeds in this case so let's register a few crawlers
      final FeedCrawlerSourceFactory crawlerFactory = new FeedCrawlerSourceFactory();

      // Polls once per minute
      final AtomTask task1 = nuke.follow(crawlerFactory.newCrawlerSource("feed-1", "http://feed.com/feed1"), new TimeValue(1, TimeUnit.MINUTES));
      task1.addListener(listener2);

      // Sets the polling interval to five minutes
      final AtomTask task2 = nuke.follow(crawlerFactory.newCrawlerSource("feed-2", "http://feed.com/feed2"), new TimeValue(5, TimeUnit.MINUTES));
      task2.addListener(listener);
      task2.addListener(listener2);

      // Sets the polling interval to one hour
      final AtomTask task3 = nuke.follow(crawlerFactory.newCrawlerSource("feed-3", "http://feed.com/feed3"), new TimeValue(1, TimeUnit.HOURS));
      task3.addListener(listener);
   }
}
