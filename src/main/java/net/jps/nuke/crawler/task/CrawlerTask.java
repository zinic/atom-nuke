package net.jps.nuke.crawler.task;

import net.jps.nuke.util.TimeValue;
import net.jps.nuke.listener.FeedListener;

/**
 *
 * @author zinic
 */
public interface CrawlerTask {

   /**
    * Cancels the crawler's next execution.
    */
   void cancel();

   boolean canceled();

   void nextLocation(String location);

   String location();

   FeedListener listener();

   TimeValue nextPollTime();
}
