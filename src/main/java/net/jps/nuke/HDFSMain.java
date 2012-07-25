package net.jps.nuke;

import net.jps.nuke.atom.stax.StaxAtomWriter;
import net.jps.nuke.crawler.FeedCrawler;
import net.jps.nuke.crawler.NukeCrawlerController;
import net.jps.nuke.listener.hadoop.HDFSFeedListener;

/**
 *
 * @author zinic
 */
public class HDFSMain {

   public static void main(String[] args) throws Exception {
      final HDFSFeedListener listener = new HDFSFeedListener("feed-name", new StaxAtomWriter());

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

      final FeedCrawler crawler = new NukeCrawlerController();

      crawler.newTask(listener, "http://feed.com/feed1");
      crawler.newTask(listener, "http://feed.com/feed2");
      crawler.newTask(listener, "http://feed.com/feed3");
      crawler.newTask(listener, "http://feed.com/feed4");
   }
}
