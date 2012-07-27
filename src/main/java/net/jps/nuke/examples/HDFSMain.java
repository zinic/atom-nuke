package net.jps.nuke.examples;

import java.util.concurrent.TimeUnit;
import net.jps.nuke.Nuke;
import net.jps.nuke.NukeKernel;
import net.jps.nuke.atom.stax.StaxAtomWriter;
import net.jps.nuke.task.Task;
import net.jps.nuke.examples.listener.hadoop.HDFSFeedListener;
import net.jps.nuke.source.crawler.FeedCrawlerSource;
import net.jps.nuke.source.crawler.FeedCrawlerSourceFactory;
import net.jps.nuke.util.TimeValue;

/**
 *
 * @author zinic
 */
public class HDFSMain {

   public static void main(String[] args) throws Exception {
      final HDFSFeedListener listener = new HDFSFeedListener("example-1.feed", new StaxAtomWriter());
      final HDFSFeedListener listener2 = new HDFSFeedListener("example-2.feed", new StaxAtomWriter());

      /*
       * Still todo...
       * 
       * - Consider using a listener delegation pattern ala SAX
       *    Might not implement this one due to the execution pattern I took. Still
       *    investigating the model.
       * 
       * - Consider using a default dir for state management that's configurable
       *    Have to re-add state management now that the execution model has been
       *    solidified.
       * 
       */

      // Create the nuke kernel
      final Nuke nuke = new NukeKernel();

      // Start nuke up
      nuke.start();

      // We want to crawl feeds in this case so let's register a few crawlers
      final FeedCrawlerSourceFactory crawlerFactory = new FeedCrawlerSourceFactory();

      // Polls for the default of once per minute
      final Task task1 = nuke.follow(crawlerFactory.newCrawlerSource("http://feed.com/feed1"));
      task1.addListener(listener2);

      // Sets the polling interval to five minutes
      final Task task2 = nuke.follow(crawlerFactory.newCrawlerSource("http://feed.com/feed2"), new TimeValue(5, TimeUnit.MINUTES));
      task2.addListener(listener);
      task2.addListener(listener2);

      // Sets the polling interval to one hour
      final Task task3 = nuke.follow(crawlerFactory.newCrawlerSource("http://feed.com/feed3"), new TimeValue(1, TimeUnit.HOURS));
      task3.addListener(listener);
   }
}
