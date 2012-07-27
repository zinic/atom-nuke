package net.jps.nuke;

import java.util.concurrent.TimeUnit;
import net.jps.nuke.atom.stax.StaxAtomWriter;
import net.jps.nuke.crawler.FeedCrawler;
import net.jps.nuke.crawler.NukeCrawlerKernel;
import net.jps.nuke.crawler.task.CrawlerTask;
import net.jps.nuke.listener.hadoop.HDFSFeedListener;
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

      // Create the crawler
      final FeedCrawler crawler = new NukeCrawlerKernel();
      
      // Start the crawler
      crawler.start();
      
      // Polls for the default of once per minute
      final CrawlerTask task1 = crawler.follow("http://feed.com/feed1");
      task1.addListener(listener2);

      // Sets the polling interval to five minutes
      final CrawlerTask task2 = crawler.follow("http://feed.com/feed2", new TimeValue(5, TimeUnit.MINUTES));
      task2.addListener(listener);
      task2.addListener(listener2);

      // Sets the polling interval to one hour
      final CrawlerTask task3 = crawler.follow("http://feed.com/feed3", new TimeValue(1, TimeUnit.HOURS));
      task3.addListener(listener);
   }
}
