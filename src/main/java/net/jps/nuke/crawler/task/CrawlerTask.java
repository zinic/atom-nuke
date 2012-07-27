package net.jps.nuke.crawler.task;

import net.jps.nuke.crawler.remote.CancellationRemote;
import net.jps.nuke.util.TimeValue;
import net.jps.nuke.listener.FeedListener;

/**
 *
 * @author zinic
 */
public interface CrawlerTask {

   /**
    * Cancels the crawler's next execution. This stops all of the listeners assigned
    * to this crawler.
    */
   void cancel();

   boolean canceled();

   String location();

   TimeValue nextPollTime();

   TimeValue interval();

   CancellationRemote addListener(FeedListener listener);

   void addListener(FeedListener listener, CancellationRemote listenerCancelationRemote);
}
